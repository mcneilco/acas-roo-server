package com.labsynch.labseer.db.migration.postgres;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.labsynch.labseer.service.SetupServiceImpl;

public class V2_0_1_1__copy_mol_structures implements SpringJdbcMigration {

	Logger logger = LoggerFactory.getLogger(SetupServiceImpl.class);

	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

		migrateParentStructures(jdbcTemplate);
		migrateLotStructures(jdbcTemplate);
		migrateSaltStructures(jdbcTemplate);
		migrateSaltFormStructures(jdbcTemplate);

	}
	
	@SuppressWarnings("unchecked")
	public void migrateParentStructures(JdbcTemplate jdbcTemplate){
		logger.info("attempting to pull out parent mol structure");

		String selectParentIds = "SELECT id FROM parent WHERE id IS NOT null";
		String selectParentByIdSQL = "SELECT * FROM parent WHERE id = ?";
		String updateParentStructure = "UPDATE parent SET mol_structure_text_temp = ? WHERE id = ?";

		List<Integer> ids = jdbcTemplate.queryForList(selectParentIds, Integer.class);

		for (Integer id : ids){
			ParentStructureObject parent = (ParentStructureObject)jdbcTemplate.queryForObject(selectParentByIdSQL, new Object[] { id }, new ParentRowMapper());
			logger.info("here is the mol_structure in the migration: " + parent.getMolStructure());
			
			int rs2 = jdbcTemplate.update(updateParentStructure,  new Object[] { parent.getMolStructure(), id });
			logger.info("number mol_structure updated: " + rs2);

		}
	}
	
	@SuppressWarnings("unchecked")
	public void migrateLotStructures(JdbcTemplate jdbcTemplate){
		logger.info("attempting to pull out lot as_drawn_struct");

		String selectLotIds = "SELECT id FROM lot";
		String selectLotByIdSQL = "SELECT * FROM lot WHERE id = ?";
		String updateLotStructure = "UPDATE lot SET as_drawn_struct_text_temp = ? WHERE id = ?";

		List<Integer> ids = jdbcTemplate.queryForList(selectLotIds, Integer.class);

		for (Integer id : ids){
			LotStructureObject lot = (LotStructureObject)jdbcTemplate.queryForObject(selectLotByIdSQL, new Object[] { id }, new LotRowMapper());
			logger.info("here is the as_drawn_struct in the migration: " + lot.getAsDrawnStruct());
			
			int rs2 = jdbcTemplate.update(updateLotStructure,  new Object[] { lot.getAsDrawnStruct(), id });
			logger.info("number as_drawn_struct updated: " + rs2);

		}
	}

	@SuppressWarnings("unchecked")
	public void migrateSaltStructures(JdbcTemplate jdbcTemplate){
		logger.info("attempting to pull out salt mol structure");

		String selectSaltIds = "SELECT id FROM salt";
		String selectSaltByIdSQL = "SELECT * FROM salt WHERE id = ?";
		String updateSaltMolStructure = "UPDATE salt SET mol_structure_text_temp = ? WHERE id = ?";
		String updateSaltOriginalStructure = "UPDATE salt SET original_structure_text_temp = ? WHERE id = ?";

		List<Integer> ids = jdbcTemplate.queryForList(selectSaltIds, Integer.class);

		for (Integer id : ids){
			SaltStructureObject salt = (SaltStructureObject)jdbcTemplate.queryForObject(selectSaltByIdSQL, new Object[] { id }, new SaltRowMapper());
			logger.info("here is the mol_structure in the migration: " + salt.getMolStructure());
			
			int rs2 = jdbcTemplate.update(updateSaltMolStructure,  new Object[] { salt.getMolStructure(), id });
			logger.info("number mol_structure updated: " + rs2);
			
			logger.info("here is the original_structure in the migration: " + salt.getOriginalStructure());
			int rs3 = jdbcTemplate.update(updateSaltOriginalStructure,  new Object[] { salt.getOriginalStructure(), id });
			logger.info("number original_structure updated: " + rs3);

		}
	}
	
	@SuppressWarnings("unchecked")
	public void migrateSaltFormStructures(JdbcTemplate jdbcTemplate){
		logger.info("attempting to pull out saltForm mol structure");

		String selectSaltFormIds = "SELECT id FROM salt_form";
		String selectSaltFormByIdSQL = "SELECT * FROM salt_form WHERE id = ?";
		String updateSaltFormStructure = "UPDATE salt_form SET mol_structure_text_temp = ? WHERE id = ?";

		List<Integer> ids = jdbcTemplate.queryForList(selectSaltFormIds, Integer.class);

		for (Integer id : ids){
			SaltFormStructureObject saltForm = (SaltFormStructureObject)jdbcTemplate.queryForObject(selectSaltFormByIdSQL, new Object[] { id }, new SaltFormRowMapper());
			logger.info("here is the mol_structure in the migration: " + saltForm.getMolStructure());
			
			int rs2 = jdbcTemplate.update(updateSaltFormStructure,  new Object[] { saltForm.getMolStructure(), id });
			logger.info("number mol_structure updated: " + rs2);

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
	
	private class LotStructureObject{
		private long id;
		private String asDrawnStruct;
		
		public long getId(){
			return this.id;
		}
		
		public String getAsDrawnStruct(){
			return this.asDrawnStruct;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setAsDrawnStruct(String asDrawnStruct){
			this.asDrawnStruct = asDrawnStruct;
		}
	}
	
	private class SaltStructureObject{
		private long id;
		private String molStructure;
		private String originalStructure;
		
		public long getId(){
			return this.id;
		}
		
		public String getMolStructure(){
			return this.molStructure;
		}
		
		public String getOriginalStructure(){
			return this.originalStructure;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setMolStructure(String molStructure){
			this.molStructure = molStructure;
		}
		
		public void setOriginalStructure(String originalStructure){
			this.originalStructure = originalStructure;
		}
	}
	
	private class SaltFormStructureObject{
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
			//Additional code to deal with the clob data
			Clob molClob = rs.getClob("mol_structure");
			InputStream in = molClob.getAsciiStream();
			StringWriter w = new StringWriter();
			try {
				IOUtils.copy(in, w);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String clobAsString = w.toString();
			parent.setMolStructure(clobAsString);
			return parent;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class LotRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			LotStructureObject lot = new LotStructureObject();
			Clob molClob = rs.getClob("as_drawn_struct");
			if (molClob!= null){
				InputStream in = molClob.getAsciiStream();
				StringWriter w = new StringWriter();
				try {
					IOUtils.copy(in, w);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String clobAsString = w.toString();
				lot.setAsDrawnStruct(clobAsString);
			}
			lot.setId(rs.getLong("id"));
			return lot;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class SaltRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SaltStructureObject salt = new SaltStructureObject();
			Clob molClob = rs.getClob("mol_structure");
			InputStream in = molClob.getAsciiStream();
			StringWriter w = new StringWriter();
			try {
				IOUtils.copy(in, w);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String clobAsString = w.toString();
			salt.setMolStructure(clobAsString);
			
			Clob molClob2 = rs.getClob("original_structure");
			InputStream in2 = molClob2.getAsciiStream();
			StringWriter w2 = new StringWriter();
			try {
				IOUtils.copy(in2, w2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String clobAsString2 = w2.toString();
			salt.setOriginalStructure(clobAsString2);
			
			salt.setId(rs.getLong("id"));
			return salt;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class SaltFormRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SaltFormStructureObject saltForm = new SaltFormStructureObject();
			Clob molClob = rs.getClob("mol_structure");
			InputStream in = molClob.getAsciiStream();
			StringWriter w = new StringWriter();
			try {
				IOUtils.copy(in, w);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String clobAsString = w.toString();
			saltForm.setMolStructure(clobAsString);
			saltForm.setId(rs.getLong("id"));
			return saltForm;
		}
	}
}

