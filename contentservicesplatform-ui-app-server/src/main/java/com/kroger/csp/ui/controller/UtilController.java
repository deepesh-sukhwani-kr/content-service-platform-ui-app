package com.kroger.csp.ui.controller;

import com.kroger.csp.ui.domain.response.ViewAngleResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Utility controller to support UI with master data
 * @author Dhriti Ghosh
 */
@RestController
@RequestMapping(value = "/imp/ui/server", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public HashMap<String, String> getEndpoints(){
        HashMap<String, String> endpoints = new HashMap<>();
        endpoints.put("add", add);
        endpoints.put("search", search);
        endpoints.put("vendorSearch", vendorSearch);
        endpoints.put("retrieval", retrieval);
        return endpoints;
    }


}
