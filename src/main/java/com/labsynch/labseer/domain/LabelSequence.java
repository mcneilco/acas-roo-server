package com.labsynch.labseer.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

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
		//first create the database sequence, then persist the object
		if (this.getDbSequence() == null) {
			String dbSequence = "labelseq_"+this.getLabelPrefix()+"_"+this.getLabelTypeAndKind()+"_"+this.getThingTypeAndKind();
			dbSequence = dbSequence.replaceAll("[^a-zA-Z0-9_]+", "_");
			this.setDbSequence(dbSequence);
		}
		if (this.getStartingNumber() < 1L) this.setStartingNumber(1L);
		EntityManager em = LabelSequence.entityManager();
		Query q = em.createNativeQuery("CREATE SEQUENCE "+this.dbSequence+" START WITH "+this.getStartingNumber());
		q.executeUpdate();
		this.persist();
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
				DialectResolver dialectResolver = new StandardDialectResolver();
				Dialect dialect =  dialectResolver.resolveDialect(new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData()));
				String currentValueString = "";
				if (dialect instanceof PostgreSQLDialect) {
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
				}else if (dialect instanceof OracleDialect) {
					throw new SQLException("Decrement sequence not implemented for Oracle");
				}else {
					throw new SQLException("Unsupported Hibernate Dialect:"+dialect.toString());
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
				DialectResolver dialectResolver = new StandardDialectResolver();
				Dialect dialect =  dialectResolver.resolveDialect(new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData()));
				try {
					String currentValueString = "";
					if (dialect instanceof PostgreSQLDialect) {
						currentValueString = "SELECT currval('"+sequenceName+"')";
					}else if (dialect instanceof OracleDialect) {
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



}
