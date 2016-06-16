package com.labsynch.labseer.db.migration.postgres;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class V1_0_4_6__copy_blob_values implements SpringJdbcMigration {

	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

		migrateBlobValue("analysis_group_value", jdbcTemplate);
//		migrateBlobValue("author_value", jdbcTemplate);
		migrateBlobValue("container_value", jdbcTemplate);
		migrateBlobValue("experiment_value", jdbcTemplate);
		migrateBlobValue("itx_container_container_value", jdbcTemplate);
		migrateBlobValue("itx_expt_expt_value", jdbcTemplate);
		migrateBlobValue("itx_ls_thing_ls_thing_value", jdbcTemplate);
		migrateBlobValue("itx_protocol_protocol_value", jdbcTemplate);
		migrateBlobValue("itx_subject_container_value", jdbcTemplate);
		migrateBlobValue("ls_thing_value", jdbcTemplate);
		migrateBlobValue("protocol_value", jdbcTemplate);
		migrateBlobValue("subject_value", jdbcTemplate);
		migrateBlobValue("treatment_group_value", jdbcTemplate);

	}
	
	@SuppressWarnings("unchecked")
	public void migrateBlobValue(String table_name, JdbcTemplate jdbcTemplate){
		String selectIds = "SELECT id FROM "+table_name+" WHERE id IS NOT null AND blob_value IS NOT NULL";
		String selectBlobValueById = "SELECT id, blob_value FROM "+table_name+" WHERE id = ?";
		String updateBlobValue = "UPDATE "+table_name+" SET blob_value_temp = ? WHERE id = ?";
		
		List<Long> ids = jdbcTemplate.queryForList(selectIds, Long.class);
		
		for (Long id : ids){
			BlobValueObject blobValue = (BlobValueObject) jdbcTemplate.queryForObject(selectBlobValueById, new Object[] { id }, new BlobValueRowMapper());
			int rs2 = jdbcTemplate.update(updateBlobValue, new Object[] {blobValue.getBlobValue(), id});
		}
	}
	
	private class BlobValueObject{
		private long id;
		private byte[] blobValue;
		
		public long getId(){
			return this.id;
		}
		
		public byte[] getBlobValue(){
			return this.blobValue;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setBlobValue(byte[] blobValue){
			this.blobValue = blobValue;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class BlobValueRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			BlobValueObject blobValue = new BlobValueObject();
			blobValue.setId(rs.getLong("id"));
			//Additional code to deal with the blob data
			Blob blob = rs.getBlob("blob_value");
			int blobLength = (int) blob.length();  
			byte[] blobAsBytes = blob.getBytes(1, blobLength);
			blobValue.setBlobValue(blobAsBytes);
			return blobValue;
		}
	}	
	
}
