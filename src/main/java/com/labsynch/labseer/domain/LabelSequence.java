package com.labsynch.labseer.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "LABEL_SEQUENCE_PKSEQ", finders = { "findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals", 
		"findLabelSequencesByThingTypeAndKindEquals", "findLabelSequencesByLabelTypeAndKindEquals" })
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
	private Long latestNumber;

	@NotNull
	private boolean ignored;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date modifiedDate;

	@NotNull
	private String dbSequence;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "labelSequenceEntry", fetch =  FetchType.LAZY)
	private Set<LabelSequenceRole> labelSequenceRoles;

	public LabelSequence save() {
		//first create the database sequence, then persist the object
		EntityManager em = LabelSequence.entityManager();
		Query q = em.createNativeQuery("CREATE SEQUENCE "+this.dbSequence);
		q.executeUpdate();
		this.persist();
		return this;
	}

	public List<String> getNextLabels(int numberOfLabels){
		List<String> labels = new ArrayList<String>();
		int numGenerated = 0;
		while (numGenerated < numberOfLabels) {
			String label = this.getNextLabel();
			labels.add(label);
			numGenerated++;
		}
		return labels;
	}

	public String getNextLabel() {
		Long labelNumber = this.getID(this.getDbSequence());
		String formatLabelNumber = "%";
		formatLabelNumber = formatLabelNumber.concat("0").concat(this.getDigits().toString()).concat("d");
		String label = this.getLabelPrefix().concat(this.getLabelSeparator()).concat(String.format(formatLabelNumber, labelNumber));
		return label;

	}

	public Long getID(final String sequenceName) {
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



}
