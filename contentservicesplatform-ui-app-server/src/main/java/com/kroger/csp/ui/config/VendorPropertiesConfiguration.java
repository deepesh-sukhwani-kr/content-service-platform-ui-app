package com.kroger.csp.ui.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * Configuration class to read properties to hit vendor API's (Kwikee and Gladson)
 */
@Configuration
@ConfigurationProperties
public class VendorPropertiesConfiguration {

    /**
     * Read Kwikee properties
     * @return
     * @throws Exception
     */
    @Bean
    @Qualifier("kwikeeProperties")
    public Properties getKwikeeProperties() throws Exception {
        Resource resource = new ClassPathResource("/KwikeeContinuousImageUpdate.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }

    /**
     * Read Gladson properties
     * @return
     * @throws Exception
     */
    @Bean
    @Qualifier("gladsonProperties")
    public Properties getGladsonProperties() throws Exception {
        Resource resource = new ClassPathResource("/GladsonContinuousImageUpdate.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }

    /**
     * Read Syndigo properties
     * @return
     * @throws Exception
     */
    @Bean
    @Qualifier("syndigoProperties")
    public Properties getSyndigoProperties() throws Exception {
        Resource resource = new ClassPathResource("/SyndigoContinuousImageUpdate.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }
}
