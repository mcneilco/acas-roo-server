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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class V1_0_4_2__copy_clob_values implements SpringJdbcMigration {

	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

		migrateClobValue("analysis_group_value", jdbcTemplate);
//		migrateClobValue("author_value", jdbcTemplate);
		migrateClobValue("container_value", jdbcTemplate);
		migrateClobValue("experiment_value", jdbcTemplate);
		migrateClobValue("itx_container_container_value", jdbcTemplate);
		migrateClobValue("itx_expt_expt_value", jdbcTemplate);
		migrateClobValue("itx_ls_thing_ls_thing_value", jdbcTemplate);
		migrateClobValue("itx_protocol_protocol_value", jdbcTemplate);
		migrateClobValue("itx_subject_container_value", jdbcTemplate);
		migrateClobValue("ls_thing_value", jdbcTemplate);
		migrateClobValue("protocol_value", jdbcTemplate);
		migrateClobValue("subject_value", jdbcTemplate);
		migrateClobValue("treatment_group_value", jdbcTemplate);
		
		migratePageContent("thing_page", jdbcTemplate);
		migratePageContent("thing_page_archive", jdbcTemplate);

	}
	
	@SuppressWarnings("unchecked")
	public void migrateClobValue(String table_name, JdbcTemplate jdbcTemplate){
		String selectIds = "SELECT id FROM "+table_name+" WHERE id IS NOT null AND clob_value IS NOT NULL";
		String selectClobValueById = "SELECT id, clob_value FROM "+table_name+" WHERE id = ?";
		String updateClobValue = "UPDATE "+table_name+" SET clob_value_temp = ? WHERE id = ?";
		
		List<Long> ids = jdbcTemplate.queryForList(selectIds, Long.class);
		
		for (Long id : ids){
			ClobValueObject clobValue = (ClobValueObject) jdbcTemplate.queryForObject(selectClobValueById, new Object[] { id }, new ClobValueRowMapper());
			int rs2 = jdbcTemplate.update(updateClobValue, new Object[] {clobValue.getClobValue(), id});
		}
	}
	
	private class ClobValueObject{
		private long id;
		private String clobValue;
		
		public long getId(){
			return this.id;
		}
		
		public String getClobValue(){
			return this.clobValue;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setClobValue(String clobValue){
			this.clobValue = clobValue;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class ClobValueRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClobValueObject clobValue = new ClobValueObject();
			clobValue.setId(rs.getLong("id"));
			//Additional code to deal with the clob data
			Clob molClob = rs.getClob("clob_value");
			InputStream in = molClob.getAsciiStream();
			StringWriter w = new StringWriter();
			try {
				IOUtils.copy(in, w);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String clobAsString = w.toString();
			clobValue.setClobValue(clobAsString);
			return clobValue;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void migratePageContent(String table_name, JdbcTemplate jdbcTemplate){
		String selectIds = "SELECT id FROM "+table_name+" WHERE id IS NOT null";
		String selectPageContentById = "SELECT id, page_content FROM "+table_name+" WHERE id = ?";
		String updatePageContent = "UPDATE "+table_name+" SET page_content_temp = ? WHERE id = ?";
		
		List<Long> ids = jdbcTemplate.queryForList(selectIds, Long.class);
		
		for (Long id : ids){
			PageContentObject pageContent = (PageContentObject) jdbcTemplate.queryForObject(selectPageContentById, new Object[] { id }, new PageContentRowMapper());
			int rs2 = jdbcTemplate.update(updatePageContent, new Object[] {pageContent.getPageContent(), id});
		}
	}
	
	private class PageContentObject{
		private long id;
		private String pageContent;
		
		public long getId(){
			return this.id;
		}
		
		public String getPageContent(){
			return this.pageContent;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setPageContent(String pageContent){
			this.pageContent = pageContent;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class PageContentRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			PageContentObject pageContent = new PageContentObject();
			pageContent.setId(rs.getLong("id"));
			//Additional code to deal with the clob data
			Clob molClob = rs.getClob("page_content");
			InputStream in = molClob.getAsciiStream();
			StringWriter w = new StringWriter();
			try {
				IOUtils.copy(in, w);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String clobAsString = w.toString();
			pageContent.setPageContent(clobAsString);
			return pageContent;
		}
	}
	
	
}
