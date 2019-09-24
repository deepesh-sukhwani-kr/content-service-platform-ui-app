package com.kroger.csp.ui.controller;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.service.GladsonSearchService;
import com.kroger.csp.ui.service.KwikeeSearchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imp/ui/v1/server")

public class VendorSearchController {

    @Autowired
    private KwikeeSearchService kwikeeSearchService;

    @Autowired
    private GladsonSearchService gladsonSearchService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/vendorSearch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public VendorSearchResponse vendorSearch(@RequestParam(value = "vendorSource", required = true) String vendorSource,
                                             @RequestParam(value = "gtin", required = true) String gtin) {

        VendorSearchResponse vendorSearchResponse = new VendorSearchResponse();
        try {

            if (StringUtils.equalsIgnoreCase(vendorSource, "KWIKEE")) {
                vendorSearchResponse = kwikeeSearchService.getImageDetailsByGtin(gtin);
            }else if(StringUtils.equalsIgnoreCase(vendorSource, "GLADSON")) {
                vendorSearchResponse = gladsonSearchService.getImageDetailsByGtin(gtin);
            }
            System.out.println("processed: " + vendorSearchResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finished!");
        return vendorSearchResponse;
    }
}
