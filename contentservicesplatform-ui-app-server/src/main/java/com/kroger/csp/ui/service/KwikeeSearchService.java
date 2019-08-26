package com.kroger.csp.ui.service;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import com.kroger.imp.apm.KwikeeAPI;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@Service
public class KwikeeSearchService {

    @Autowired
    @Qualifier("kwikeeProperties")
    Properties properties;

    public VendorSearchResponse getImageDetailsByGtin(String gtin) throws Exception {


        VendorSearchResponse vendorSearchResponse = new VendorSearchResponse();
        List<VendorSearchViewAngleResponse> viewAngleResponseList = new ArrayList<>();

        try {
            KwikeeAPI kwikeeAPI = new KwikeeAPI(properties);
            String kwikeeSearchRespJSON = kwikeeAPI.getItemDataJSON(gtin);
            JSONObject jsonAssetObject = new JSONObject(kwikeeSearchRespJSON);

            String description = jsonAssetObject.getJSONArray("customProductName").getJSONObject(0).getString("text").toString();
            String source = "KWIKEE";

            Map<String, String> searchResults = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            searchResults = kwikeeAPI.imageSearch(gtin);

            for(String viewAngle: searchResults.keySet()){
                VendorSearchViewAngleResponse vendorSearchViewAngleResponse = new VendorSearchViewAngleResponse();
                vendorSearchViewAngleResponse.setViewAngle(viewAngle);
                //String base64 = kwikeeAPI.getAssetFromURL(searchResults.get(viewAngle));
                //vendorSearchViewAngleResponse.setBase64(base64);
                vendorSearchViewAngleResponse.setUrl(searchResults.get(viewAngle));
                viewAngleResponseList.add(vendorSearchViewAngleResponse);
            }

            vendorSearchResponse.setDescription(description);
            vendorSearchResponse.setGtin(gtin);
            vendorSearchResponse.setImageType("jpg");
            vendorSearchResponse.setSource(source);
            vendorSearchResponse.setViewAngleList(viewAngleResponseList);

        } catch (Exception e) {

        }
        return vendorSearchResponse;
    }

}
