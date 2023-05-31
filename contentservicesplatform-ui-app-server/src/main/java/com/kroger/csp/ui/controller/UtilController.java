package com.kroger.csp.ui.controller;

import com.kroger.csp.ui.config.RBACConfiguration;
import com.kroger.csp.ui.domain.response.RBACResponse;
import com.kroger.csp.ui.domain.response.ViewAngleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility controller to support UI with master data
 * @author Dhriti Ghosh
 */
@RestController
@RequestMapping(value = "/imp/ui/server", produces = MediaType.APPLICATION_JSON_VALUE)
@RefreshScope
public class UtilController {

    @Value("${kroger.imagedata.viewangles}")
    private String[] viewAngles;
    @Value("${kroger.serverendpoints.add:}")
    private String add;
    @Value("${kroger.serverendpoints.search:}")
    private String search;
    @Value("${kroger.serverendpoints.vendorsearch:}")
    private String vendorSearch;
    @Value("${kroger.serverendpoints.retrieval:}")
    private String retrieval;
    @Autowired
    private RBACConfiguration rbacConfiguration;

    /**
     * Get the view angles supported
     * @return
     */
    @GetMapping(path = "/viewAngles")
    public ViewAngleResponse getViewAngles(){
        ViewAngleResponse response = new ViewAngleResponse();
        response.setViewAngles(Arrays.asList(this.viewAngles));
        return response;
    }

    /**
     * Get the UI backend server endpoints
     * @return
     */
    @GetMapping(path = "/endpoints")
    public Map<String, String> getEndpoints(){
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("add", add);
        endpoints.put("search", search);
        endpoints.put("vendorSearch", vendorSearch);
        endpoints.put("retrieval", retrieval);
        return endpoints;
    }

    @GetMapping(path = "/rbacConfig")
    public RBACResponse getAddImageAuthorizedRoles(){
        RBACResponse response = new RBACResponse();
        response.setAddRoles(rbacConfiguration.getAddRoles());
        response.setCheckRbac(rbacConfiguration.isCheckRbac());
        response.setExternalSources(rbacConfiguration.getExternalSources());
        response.setKrogerExternalRoles(rbacConfiguration.getKrogerExternalRoles());
        response.setSearchRoles(rbacConfiguration.getSearchRoles());
        response.setVendorAddRoles(rbacConfiguration.getVendorAddRoles());
        return response;
    }

}
