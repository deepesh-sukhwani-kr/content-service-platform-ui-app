package com.kroger.csp.ui.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

@Configuration
@ConfigurationProperties
public class VendorPropertiesConfiguration {

    @Bean
    @Qualifier("kwikeeProperties")
    public Properties getKwikeeProperties() throws Exception {
        Resource resource = new ClassPathResource("/KwikeeContinuousImageUpdate.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }

    @Bean
    @Qualifier("gladsonProperties")
    public Properties getGladsonProperties() throws Exception {
        Resource resource = new ClassPathResource("/GladsonContinuousImageUpdate.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }
}
