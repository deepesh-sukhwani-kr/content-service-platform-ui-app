package com.kroger.csp.ui.controller;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.service.GladsonSearchService;
import com.kroger.csp.ui.service.KwikeeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to handle Vendor Searching
 */
@RestController
@RequestMapping(value = "/imp/ui/v1/server", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VendorSearchController {

    @Autowired
    private KwikeeSearchService kwikeeService;
    @Autowired
    private GladsonSearchService gladsonService;

    /**
     *
     * @param vendorSource
     * @param gtin
     * @return
     */
    @GetMapping(value = "/vendorSearch")
    public VendorSearchResponse vendorSearch(
            @RequestParam(value = "vendorSource", required = true) String vendorSource,
            @RequestParam(value = "gtin", required = true) String gtin) {
        VendorSearchResponse response = new VendorSearchResponse();
        try {
            if(StringUtils.equalsIgnoreCase(vendorSource, Vendor.KWIKEE.name()))
                response = kwikeeService.getImageDetailsByGtin(gtin);
            else if(StringUtils.equalsIgnoreCase(vendorSource, Vendor.GLADSON.name()))
                response = gladsonService.getImageDetailsByGtin(gtin);
        } catch (Exception e) {
            log.error("Error in Vendor Search - UI : " + e);
        }
        return response;
    }

    /**
     * Constants to represent vendors
     */
    private enum Vendor{ KWIKEE, GLADSON}
}
