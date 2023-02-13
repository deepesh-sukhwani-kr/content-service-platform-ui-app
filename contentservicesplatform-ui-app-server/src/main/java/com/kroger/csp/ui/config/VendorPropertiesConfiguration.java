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
 * Configuration class to read properties to hit vendor API's (Syndigo)
 */
@Configuration
@ConfigurationProperties
public class VendorPropertiesConfiguration {

    /**
     * Read Syndigo properties
     * @return
     * @throws Exception
     */
    /*
    @Bean
    @Qualifier("syndigoProperties")
    public Properties getSyndigoProperties() throws Exception {
        Resource resource = new ClassPathResource("/SyndigoContinuousImageUpdate.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }*/
}
