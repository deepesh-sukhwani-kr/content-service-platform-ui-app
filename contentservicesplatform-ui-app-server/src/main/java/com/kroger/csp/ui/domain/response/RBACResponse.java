package com.kroger.csp.ui.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Dhriti Ghosh
 */
@Setter
@Getter
public class RBACResponse {
    private boolean checkRbac;
    private List<String> addRoles;
    private List<String> vendorAddRoles;
    private List<String> searchRoles;
    private List<String> krogerExternalRoles;
    private List<String> externalSources;
}
