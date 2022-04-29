package com.labsynch.labseer.domain;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.utils.SimpleUtil;
import com.labsynch.labseer.utils.SimpleUtil.DbType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "LABEL_SEQUENCE_PKSEQ", finders = { "findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals", 
		"findLabelSequencesByThingTypeAndKindEquals", "findLabelSequencesByLabelTypeAndKindEquals",
"findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals"})
public class LabelSequence {

	@NotNull
	@Size(max = 255)
	private String thingTypeAndKind;

	@NotNull
	@Size(max = 255)
	private String labelTypeAndKind;

	@NotNull
	@Size(max = 50)
	private String labelPrefix;

	@Size(max = 10)
	private String labelSeparator;

	private boolean groupDigits;

	private Integer digits;

	@NotNull
	private Long startingNumber;

	@NotNull
	private boolean ignored;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date modifiedDate;

	@NotNull
	private String dbSequence;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "labelSequenceEntry", fetch =  FetchType.LAZY)
	private Set<LabelSequenceRole> labelSequenceRoles;

	@Transactional
	public LabelSequence save() {
		if (this.getLabelSequenceRoles() != null) {
			for (LabelSequenceRole labelSeqRole : this.getLabelSequenceRoles()) {
				labelSeqRole.setLabelSequenceEntry(this);
				if (labelSeqRole.getRoleEntry() != null && labelSeqRole.getRoleEntry().getId() != null) {
					LsRole savedRole = LsRole.findLsRole(labelSeqRole.getRoleEntry().getId());
					labelSeqRole.setRoleEntry(savedRole);
				}
			}
		}
		EntityManager em = LabelSequence.entityManager();
		if (this.getDbSequence() == null) {
			//set to temp sequence first so we can persist, get and id and then use the id to create a unique sequence name
			this.setDbSequence("tempseq");
			this.persist();
			String dbSequence = "labelseq_"+this.getId()+"_"+this.getLabelPrefix()+"_"+this.getLabelTypeAndKind()+"_"+this.getThingTypeAndKind();
			dbSequence = dbSequence.replaceAll("[^a-zA-Z0-9_]+", "_");
			//Limit to 30 characters to be compatible with Oracle Version <= 12.1 if applicable
			String databaseType = null;
			Float databaseVersion = 0.0f;
			try{
				org.hibernate.engine.spi.SessionImplementor sessionImp = 
					     (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
				DatabaseMetaData metadata = sessionImp.connection().getMetaData();
				databaseType = metadata.getDatabaseProductName();
				databaseVersion = Float.parseFloat(metadata.getDatabaseMajorVersion()+"."+metadata.getDatabaseMinorVersion());
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(databaseType.equalsIgnoreCase("Oracle") && (databaseVersion < 12.2f)) {
				int MAX_CHAR = 30;
				int maxLength = (dbSequence.length() < MAX_CHAR)?dbSequence.length():MAX_CHAR;
				dbSequence = dbSequence.substring(0, maxLength);
			}
			this.setDbSequence(dbSequence);
		}
		if (this.getStartingNumber() < 1L) this.setStartingNumber(1L);
		Query q = em.createNativeQuery("CREATE SEQUENCE "+this.dbSequence+" START WITH "+this.getStartingNumber());
		q.executeUpdate();
		this.merge();
		return this;
	}

	public List<AutoLabelDTO> generateNextLabels(Long numberOfLabels){
		List<AutoLabelDTO> labels = new ArrayList<AutoLabelDTO>();
		int numGenerated = 0;
		while (numGenerated < numberOfLabels) {
			AutoLabelDTO label = this.generateNextLabel();
			labels.add(label);
			numGenerated++;
		}
		return labels;
	}

	public AutoLabelDTO generateNextLabel() {
		Long labelNumber = this.incrementSequence(this.getDbSequence());
		String formatLabelNumber = "%";
		formatLabelNumber = formatLabelNumber.concat("0").concat(this.getDigits().toString()).concat("d");
		String label = this.getLabelPrefix().concat(this.getLabelSeparator()).concat(String.format(formatLabelNumber, labelNumber));
		AutoLabelDTO autoLabel = new AutoLabelDTO(label, labelNumber);
		return autoLabel;

	}

	public Long incrementSequence(final String sequenceName) {
		ReturningWork<Long> maxReturningWork = new ReturningWork<Long>() {
			@Override
			public Long execute(Connection connection) throws SQLException {
				DialectResolver dialectResolver = new StandardDialectResolver();
				Dialect dialect =  dialectResolver.resolveDialect(new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData()));
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				try {
					preparedStatement = connection.prepareStatement( dialect.getSequenceNextValString(sequenceName));
					resultSet = preparedStatement.executeQuery();
					resultSet.next();
					return resultSet.getLong(1);
				}catch (SQLException e) {
					throw e;
				} finally {
					if(preparedStatement != null) {
						preparedStatement.close();
					}
					if(resultSet != null) {
						resultSet.close();
					}
				}

			}
		};
		EntityManager em = LabelSequence.entityManager();
		Long maxRecord = em.unwrap(Session.class).doReturningWork(maxReturningWork);
		return maxRecord;
	}

	@Transactional
	public Long decrementSequence() {
		final String sequenceName = this.getDbSequence();
		ReturningWork<Long> maxReturningWork = new ReturningWork<Long>() {
			@Override
			public Long execute(Connection connection) throws SQLException {
				String currentValueString = "";
				DbType dbType = SimpleUtil.getDatabaseType(connection.getMetaData());
				if (dbType == DbType.POSTGRES) {
					currentValueString = "SELECT currval('"+sequenceName+"')";
					long currentValue;
					try (PreparedStatement preparedStatement = connection.prepareStatement( currentValueString);
							ResultSet resultSet = preparedStatement.executeQuery()) {
						resultSet.next();
						currentValue = resultSet.getLong(1);
					}
					String setValueString = "SELECT setval('"+sequenceName+"', "+currentValue+")";
					try (PreparedStatement preparedStatement = connection.prepareStatement( setValueString);
							ResultSet resultSet = preparedStatement.executeQuery();){
						resultSet.next();
						return resultSet.getLong(1);	
					}
				}else if (dbType == DbType.ORACLE) {
					throw new SQLException("Decrement sequence not implemented for Oracle");
				}else {
					throw new SQLException("Unssupported database type");
				}
			}
		};
		EntityManager em = LabelSequence.entityManager();
		Long maxRecord = em.unwrap(Session.class).doReturningWork(maxReturningWork);
		return maxRecord;
	}

	public long fetchCurrentValue() {
		final String sequenceName = this.getDbSequence();
		ReturningWork<Long> maxReturningWork = new ReturningWork<Long>() {
			@Override
			public Long execute(Connection connection) throws SQLException {
				DbType dbType = SimpleUtil.getDatabaseType(connection.getMetaData());
				try {
					String currentValueString = "";
					if (dbType == DbType.POSTGRES) {
						currentValueString = "SELECT last_value FROM "+sequenceName;
					}else if (dbType == DbType.ORACLE) {
						currentValueString = "SELECT last_number FROM all_sequences WHERE sequence_name = '"+sequenceName+"'";
					}
					try (
							PreparedStatement preparedStatement = connection.prepareStatement( currentValueString);
							ResultSet resultSet = preparedStatement.executeQuery()) {
						resultSet.next();
						return resultSet.getLong(1);
					}
				}catch (SQLException e) {
					throw e;
				}

			}
		};
		EntityManager em = LabelSequence.entityManager();
		Long maxRecord = em.unwrap(Session.class).doReturningWork(maxReturningWork);
		return maxRecord;
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class","labelSequenceRoles.labelSequenceEntry").include("labelSequenceRoles.roleEntry").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public static String toJsonArray(Collection<LabelSequence> collection) {
		return new JSONSerializer().exclude("*.class","labelSequenceRoles.labelSequenceEntry").include("labelSequenceRoles.roleEntry").transform(new ExcludeNulls(), void.class).serialize(collection);
	}




	public static Long countFindLabelSequencesByLabelTypeAndKindEquals(String labelTypeAndKind) {
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelSequence AS o WHERE o.labelTypeAndKind = :labelTypeAndKind", Long.class);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLabelSequencesByThingTypeAndKindEquals(String thingTypeAndKind) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind", Long.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(String thingTypeAndKind, String labelTypeAndKind) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind  AND o.labelTypeAndKind = :labelTypeAndKind", Long.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(String thingTypeAndKind, String labelTypeAndKind, String labelPrefix) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        if (labelPrefix == null || labelPrefix.length() == 0) throw new IllegalArgumentException("The labelPrefix argument is required");
        EntityManager em = LabelSequence.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind  AND o.labelTypeAndKind = :labelTypeAndKind  AND o.labelPrefix = :labelPrefix", Long.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        q.setParameter("labelPrefix", labelPrefix);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<LabelSequence> findLabelSequencesByLabelTypeAndKindEquals(String labelTypeAndKind) {
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        TypedQuery<LabelSequence> q = em.createQuery("SELECT o FROM LabelSequence AS o WHERE o.labelTypeAndKind = :labelTypeAndKind", LabelSequence.class);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        return q;
    }

	public static TypedQuery<LabelSequence> findLabelSequencesByLabelTypeAndKindEquals(String labelTypeAndKind, String sortFieldName, String sortOrder) {
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelSequence AS o WHERE o.labelTypeAndKind = :labelTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelSequence> q = em.createQuery(queryBuilder.toString(), LabelSequence.class);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        return q;
    }

	public static TypedQuery<LabelSequence> findLabelSequencesByThingTypeAndKindEquals(String thingTypeAndKind) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        TypedQuery<LabelSequence> q = em.createQuery("SELECT o FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind", LabelSequence.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        return q;
    }

	public static TypedQuery<LabelSequence> findLabelSequencesByThingTypeAndKindEquals(String thingTypeAndKind, String sortFieldName, String sortOrder) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelSequence> q = em.createQuery(queryBuilder.toString(), LabelSequence.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        return q;
    }

	public static TypedQuery<LabelSequence> findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(String thingTypeAndKind, String labelTypeAndKind) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        TypedQuery<LabelSequence> q = em.createQuery("SELECT o FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind  AND o.labelTypeAndKind = :labelTypeAndKind", LabelSequence.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        return q;
    }

	public static TypedQuery<LabelSequence> findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(String thingTypeAndKind, String labelTypeAndKind, String sortFieldName, String sortOrder) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        EntityManager em = LabelSequence.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind  AND o.labelTypeAndKind = :labelTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelSequence> q = em.createQuery(queryBuilder.toString(), LabelSequence.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        return q;
    }

	public static TypedQuery<LabelSequence> findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(String thingTypeAndKind, String labelTypeAndKind, String labelPrefix) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        if (labelPrefix == null || labelPrefix.length() == 0) throw new IllegalArgumentException("The labelPrefix argument is required");
        EntityManager em = LabelSequence.entityManager();
        TypedQuery<LabelSequence> q = em.createQuery("SELECT o FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind  AND o.labelTypeAndKind = :labelTypeAndKind  AND o.labelPrefix = :labelPrefix", LabelSequence.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        q.setParameter("labelPrefix", labelPrefix);
        return q;
    }

	public static TypedQuery<LabelSequence> findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(String thingTypeAndKind, String labelTypeAndKind, String labelPrefix, String sortFieldName, String sortOrder) {
        if (thingTypeAndKind == null || thingTypeAndKind.length() == 0) throw new IllegalArgumentException("The thingTypeAndKind argument is required");
        if (labelTypeAndKind == null || labelTypeAndKind.length() == 0) throw new IllegalArgumentException("The labelTypeAndKind argument is required");
        if (labelPrefix == null || labelPrefix.length() == 0) throw new IllegalArgumentException("The labelPrefix argument is required");
        EntityManager em = LabelSequence.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelSequence AS o WHERE o.thingTypeAndKind = :thingTypeAndKind  AND o.labelTypeAndKind = :labelTypeAndKind  AND o.labelPrefix = :labelPrefix");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelSequence> q = em.createQuery(queryBuilder.toString(), LabelSequence.class);
        q.setParameter("thingTypeAndKind", thingTypeAndKind);
        q.setParameter("labelTypeAndKind", labelTypeAndKind);
        q.setParameter("labelPrefix", labelPrefix);
        return q;
    }

	@Id
    @SequenceGenerator(name = "labelSequenceGen", sequenceName = "LABEL_SEQUENCE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "labelSequenceGen")
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getThingTypeAndKind() {
        return this.thingTypeAndKind;
    }

	public void setThingTypeAndKind(String thingTypeAndKind) {
        this.thingTypeAndKind = thingTypeAndKind;
    }

	public String getLabelTypeAndKind() {
        return this.labelTypeAndKind;
    }

	public void setLabelTypeAndKind(String labelTypeAndKind) {
        this.labelTypeAndKind = labelTypeAndKind;
    }

	public String getLabelPrefix() {
        return this.labelPrefix;
    }

	public void setLabelPrefix(String labelPrefix) {
        this.labelPrefix = labelPrefix;
    }

	public String getLabelSeparator() {
        return this.labelSeparator;
    }

	public void setLabelSeparator(String labelSeparator) {
        this.labelSeparator = labelSeparator;
    }

	public boolean isGroupDigits() {
        return this.groupDigits;
    }

	public void setGroupDigits(boolean groupDigits) {
        this.groupDigits = groupDigits;
    }

	public Integer getDigits() {
        return this.digits;
    }

	public void setDigits(Integer digits) {
        this.digits = digits;
    }

	public Long getStartingNumber() {
        return this.startingNumber;
    }

	public void setStartingNumber(Long startingNumber) {
        this.startingNumber = startingNumber;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public String getDbSequence() {
        return this.dbSequence;
    }

	public void setDbSequence(String dbSequence) {
        this.dbSequence = dbSequence;
    }

	public Set<LabelSequenceRole> getLabelSequenceRoles() {
        return this.labelSequenceRoles;
    }

	public void setLabelSequenceRoles(Set<LabelSequenceRole> labelSequenceRoles) {
        this.labelSequenceRoles = labelSequenceRoles;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("thingTypeAndKind", "labelTypeAndKind", "labelPrefix", "labelSeparator", "groupDigits", "digits", "startingNumber", "ignored", "modifiedDate", "dbSequence", "labelSequenceRoles");

	public static final EntityManager entityManager() {
        EntityManager em = new LabelSequence().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLabelSequences() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LabelSequence o", Long.class).getSingleResult();
    }

	public static List<LabelSequence> findAllLabelSequences() {
        return entityManager().createQuery("SELECT o FROM LabelSequence o", LabelSequence.class).getResultList();
    }

	public static List<LabelSequence> findAllLabelSequences(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LabelSequence o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LabelSequence.class).getResultList();
    }

	public static LabelSequence findLabelSequence(Long id) {
        if (id == null) return null;
        return entityManager().find(LabelSequence.class, id);
    }

	public static List<LabelSequence> findLabelSequenceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LabelSequence o", LabelSequence.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LabelSequence> findLabelSequenceEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LabelSequence o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LabelSequence.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LabelSequence attached = LabelSequence.findLabelSequence(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public LabelSequence merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LabelSequence merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static LabelSequence fromJsonToLabelSequence(String json) {
        return new JSONDeserializer<LabelSequence>()
        .use(null, LabelSequence.class).deserialize(json);
    }

	public static Collection<LabelSequence> fromJsonArrayToLabelSequences(String json) {
        return new JSONDeserializer<List<LabelSequence>>()
        .use("values", LabelSequence.class).deserialize(json);
    }
}
