package com.labsynch.labseer.db.migration.sqlserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;

public class V2_0_7_1__Recalculate_exact_mass implements SpringJdbcMigration {
 
	Logger logger = LoggerFactory.getLogger(V2_0_7_1__Recalculate_exact_mass.class);
	
	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Transactional
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		logger.debug("attempting to pull out parent mol structure");

		String selectParentIds = "SELECT id FROM parent WHERE id IS NOT null";
		String selectParentByIdSQL = "SELECT * FROM parent WHERE id = ?";
		String updateParentWeights = "UPDATE parent SET mol_weight = ?, exact_mass = ? WHERE id = ?";

		List<Integer> ids = jdbcTemplate.queryForList(selectParentIds, Integer.class);

		for (Integer id : ids){
			ParentStructureObject parent = (ParentStructureObject)jdbcTemplate.queryForObject(selectParentByIdSQL, new Object[] { id }, new ParentRowMapper());
			if (logger.isDebugEnabled()) logger.debug(parent.getMolStructure());
			Double molWeight = getMolWeight(parent.getMolStructure());
			Double exactMass = getExactMass(parent.getMolStructure());
			
			int rs2 = jdbcTemplate.update(updateParentWeights,  new Object[] { molWeight, exactMass, id });
		}
	}
	
	private class ParentStructureObject{
		private long id;
		private String molStructure;
		
		public long getId(){
			return this.id;
		}
		
		public String getMolStructure(){
			return this.molStructure;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setMolStructure(String molStructure){
			this.molStructure = molStructure;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class ParentRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ParentStructureObject parent = new ParentStructureObject();
			parent.setId(rs.getLong("id"));
			parent.setMolStructure(rs.getString("mol_structure"));
			return parent;
		}
	}
	
	private double getMolWeight(String molStructure) {
		try{
			CmpdRegMolecule molecule = cmpdRegMoleculeFactory.getCmpdRegMolecule(molStructure);
			return molecule.getMass();
		}catch (Exception e) {
			return 0d;
		}
		
	}

	private double getExactMass(String molStructure) {
		try{
			CmpdRegMolecule molecule = cmpdRegMoleculeFactory.getCmpdRegMolecule(molStructure);
			return molecule.getExactMass();
		}catch (Exception e) {
			return 0d;
		}
	}


}

