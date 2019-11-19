package com.kroger.csp.ui.controller;

import com.kroger.csp.ui.domain.request.RawAssetRequest;
import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.service.GladsonSearchService;
import com.kroger.csp.ui.service.KwikeeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to handle Vendor Searching
 */
@RestController
@RequestMapping(value = "/imp/ui/v1/server")
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
    @GetMapping(value = "/vendorSearch", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/getAssset", produces = MediaType.IMAGE_JPEG_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getImage(@RequestBody RawAssetRequest request){
        byte[] image = null;
        try {
            if(request.getVendor().trim().equalsIgnoreCase("KWIKEE"))
                image = kwikeeService.getRawImage(request.getUrl());
        }catch (Exception ex){
            log.error("Error in Vendor Search - UI : " + ex);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    /**
     * Constants to represent vendors
     */
    private enum Vendor{ KWIKEE, GLADSON}
}
