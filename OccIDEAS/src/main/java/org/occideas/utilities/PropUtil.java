package org.occideas.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropUtil {

	private static final String PROPERTY_FILENAME = "occideas.properties";

	private Logger log = LogManager.getLogger(this.getClass());

	private PropUtil() {
		init(PROPERTY_FILENAME);
	}

	private static class LazyHolder {
		private static final PropUtil INSTANCE = new PropUtil();
	}

	public static PropUtil getInstance() {
		return LazyHolder.INSTANCE;
	}

	private final Properties configProp = new Properties();

	public String getProperty(String key) {
		return configProp.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return configProp.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return configProp.containsKey(key);
	}

	protected void init(String propertyFile) {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(propertyFile);
		try {
			configProp.load(in);
		} catch (IOException e) {
			log.error("Error on " + propertyFile, e);
		}
	}

}
