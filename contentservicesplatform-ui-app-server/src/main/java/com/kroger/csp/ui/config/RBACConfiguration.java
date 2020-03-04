package com.kroger.csp.ui.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Dhriti Ghosh
 */
@Configuration
@ConfigurationProperties(prefix = "kroger.rbac")
@Getter
@Setter
public class RBACConfiguration {
    private boolean checkRbac;
    private List<String> addRoles;
    private List<String> vendorAddRoles;
    private List<String> searchRoles;
    private List<String> krogerExternalRoles;
    private List<String> externalSources;
}
