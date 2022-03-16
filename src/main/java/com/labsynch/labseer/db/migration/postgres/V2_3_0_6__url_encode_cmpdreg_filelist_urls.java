package com.labsynch.labseer.db.migration.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.net.URLEncoder;

import javax.transaction.Transactional;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;



public class V2_3_0_6__url_encode_cmpdreg_filelist_urls implements SpringJdbcMigration {
 
	Logger logger = LoggerFactory.getLogger(V2_3_0_6__url_encode_cmpdreg_filelist_urls.class);

	@Transactional
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		logger.debug("attempting to url encode file_list.urls");

		String selectFileIds = "SELECT id FROM file_list WHERE id IS NOT null";
		String selectFileByIdSQL = "SELECT * FROM file_list WHERE id = ?";
		String updateFileURLs = "UPDATE file_list SET url = ? WHERE id = ?";

		List<Integer> ids = jdbcTemplate.queryForList(selectFileIds, Integer.class);

		for (Integer id : ids){
			FileObject file = (FileObject)jdbcTemplate.queryForObject(selectFileByIdSQL, new Object[] { id }, new FileRowMapper());
			if (logger.isDebugEnabled()) logger.debug(file.getUrl());
			String url = getEncodedUrl(file.getUrl());
			
			int rs2 = jdbcTemplate.update(updateFileURLs,  new Object[] { url, id });
		}
	}
	
	private class FileObject{
		private long id;
		private String url;
		
		public long getId(){
			return this.id;
		}
		
		public String getUrl(){
			return this.url;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setUrl(String url){
			this.url = url;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class FileRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			FileObject file = new FileObject();
			file.setId(rs.getLong("id"));
			file.setUrl(rs.getString("url"));
			return file;
		}
	}
	
	private String getEncodedUrl(String url) {
		try{
			// Split url on = and encode the second part (file path) of the url
			String[] urlParts = url.split("=");
			String charset = "UTF-8";
			String encodedUrl = urlParts[0] + "=" + URLEncoder.encode(urlParts[1], charset);
			return encodedUrl;
		} catch (Exception e){
			logger.error("error encoding url: " + url);
			// log the stacktrace
			e.printStackTrace();
			return url;
		}
	}

}

