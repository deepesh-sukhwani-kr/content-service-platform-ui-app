package com.kroger.csp.ui.service;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.util.VendorUtil;
import com.kroger.imp.apm.GladsonUpdatedAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Service class to support Gladson vendor search
 */
@Service
@Slf4j
public class GladsonSearchService {

    @Autowired
    private VendorUtil vendorUtil;
    @Autowired
    @Qualifier("gladsonProperties")
    Properties properties;
    @Value("${kroger.imagedata.viewangles}")
    private String[] viewAngles;
    @Value("${kroger.imagedata.size.gladson}")
    private String providedSize;
    @Value("${kroger.imagedata.background.gladson}")
    private String background;
    @Value("${kroger.imagedata.imagetype.gladson}")
    private String imageType;

    public VendorSearchResponse getImageDetailsByGtin(String gtin) throws Exception {
        VendorSearchResponse response = new VendorSearchResponse();
        GladsonUpdatedAPI gladsonUpdatedAPI = new GladsonUpdatedAPI(properties);
        response.setGtin(gtin);
        response.setImageType(imageType);
        response.setSource("Gladson");
        response.setBackground(background);
        response.setProvidedSize(providedSize);
        response.setViewAngleList(vendorUtil.getViewAngleList(gladsonUpdatedAPI.imageSearch(gtin)));
        return response;
    }

}
