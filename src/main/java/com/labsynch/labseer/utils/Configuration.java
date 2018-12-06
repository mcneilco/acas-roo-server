package com.labsynch.labseer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.labsynch.labseer.dto.configuration.MainConfigDTO;

public class Configuration {
	
	static Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	private static InputStream configStream;

	private static String configFileName = "configuration.json";
	
	private static String configFilePath = null;

	public void setConfigFilePath(String configFilePath)
	{
		Configuration.configFilePath = configFilePath;
	}

	public static String getConfigFilePath()
	{
		return configFilePath;
	}

	public static boolean useExternalConfig = false;

	public static boolean isUseExternalConfig() {
		return useExternalConfig;
	}

	public void setUseExternalConfig(boolean useExternalConfig) {
		Configuration.useExternalConfig = useExternalConfig;
	}

	
	public static MainConfigDTO getConfigInfo() {
		String configurationString = null;
		logger.debug("In getConfigInfo class: use external config file: " + Configuration.isUseExternalConfig());
		try {
			if (Configuration.isUseExternalConfig()){
				configurationString = new String(Files.readAllBytes(Paths.get(Configuration.getConfigFilePath())));
			} else {
				configStream = MainConfigDTO.class.getClassLoader().getResourceAsStream(configFileName);
				configurationString = IOUtils.toString(configStream);
				configStream.close();
			}
		} catch (IOException e1) {
			logger.error("Unable to open the configuration file: " + configFileName);
			e1.printStackTrace();
		}

		MainConfigDTO mainConfig = MainConfigDTO.fromJsonToMainConfigDTO(configurationString);
		
		return mainConfig;
	}
	
	public String getConfigJson() {
		String configurationString = null;
		logger.debug("In Configuraiton.json class: use external config file: " + Configuration.isUseExternalConfig());
		try {
			if (Configuration.isUseExternalConfig()){
				configurationString = new String(Files.readAllBytes(Paths.get(Configuration.getConfigFilePath())));
			} else {
				configStream = MainConfigDTO.class.getClassLoader().getResourceAsStream(configFileName);
				configurationString = IOUtils.toString(configStream);
				configStream.close();
			}
		} catch (IOException e1) {
			logger.error("Unable to open the configuration file: " + configFileName);
			e1.printStackTrace();
		}
		
		return configurationString;
	}
	
}
