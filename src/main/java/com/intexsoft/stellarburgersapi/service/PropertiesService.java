package com.intexsoft.stellarburgersapi.service;

import java.io.IOException;
import java.util.Properties;

public class PropertiesService {
    private static final String CONFIG_FILENAME = "config.properties";
    private static final String TESTDATA_FILENAME = "testdata.properties";
    private static final Properties CONFIG_PROPERTIES = new Properties();
    private static final Properties TESTDATA_PROPERTIES = new Properties();

    static {
        loadProperties(CONFIG_PROPERTIES, CONFIG_FILENAME);
        loadProperties(TESTDATA_PROPERTIES, TESTDATA_FILENAME);
    }

    private static void loadProperties(Properties properties, String propertiesFileName) {
        try {
            properties.load(PropertiesService.class.getResourceAsStream(propertiesFileName));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read or find properties file " + propertiesFileName + " from resources.");
        }
    }

    public static String getProperty(PropertiesFile propertiesFile, String propertyKey) {
        switch (propertiesFile) {
            case CONFIG:
                return CONFIG_PROPERTIES.getProperty(propertyKey);
            case TESTDATA:
                return TESTDATA_PROPERTIES.getProperty(propertyKey);
            default:
                throw new RuntimeException("Property file type not yet implemented in getProperty method.");
        }
    }
}
