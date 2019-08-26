package com.kroger.csp.ui.service;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import com.kroger.imp.apm.GladsonUpdatedAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@Service
public class GladsonSearchService {

    @Autowired
    @Qualifier("gladsonProperties")
    Properties properties;

    public VendorSearchResponse getImageDetailsByGtin(String gtin) throws Exception {


        VendorSearchResponse vendorSearchResponse = new VendorSearchResponse();
        List<VendorSearchViewAngleResponse> viewAngleResponseList = new ArrayList<>();

        try {

            GladsonUpdatedAPI gladsonUpdatedAPI = new GladsonUpdatedAPI(properties);

            Map<String, String> searchResultsMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            searchResultsMap = gladsonUpdatedAPI.imageSearch(gtin);

            for(String viewAngle: searchResultsMap.keySet()){
                VendorSearchViewAngleResponse vendorSearchViewAngleResponse = new VendorSearchViewAngleResponse();
                vendorSearchViewAngleResponse.setViewAngle(viewAngle);
                vendorSearchViewAngleResponse.setUrl(searchResultsMap.get(viewAngle));
                viewAngleResponseList.add(vendorSearchViewAngleResponse);
            }

            //vendorSearchResponse.setDescription(description);
            vendorSearchResponse.setGtin(gtin);
            vendorSearchResponse.setImageType("jpg");
            vendorSearchResponse.setSource("Gladson");
            vendorSearchResponse.setViewAngleList(viewAngleResponseList);

        } catch (Exception e) {

        }
        return vendorSearchResponse;
    }

    private String getBase64OfImageFromPath (String filePath, String fileName){
        String base64String = null;

        return base64String;
    }
}
