package com.kroger.csp.ui.service;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import com.kroger.csp.ui.util.VendorUtil;
import com.kroger.imp.apm.KwikeeAPI;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
@Slf4j
public class KwikeeSearchService {

    @Autowired
    @Qualifier("kwikeeProperties")
    Properties properties;

    @Value("${kroger.imagedata.viewangles}")
    private String[] viewAngles;

    @Value("${kroger.imagedata.size.kwikee}")
    private String providedSize;

    @Value("${kroger.imagedata.background.kwikee}")
    private String background;

    @Value("${kroger.imagedata.imagetype.kwikee}")
    private String imageType;

    @Autowired
    private VendorUtil vendorUtil;

    public VendorSearchResponse getImageDetailsByGtin(String gtin){
        VendorSearchResponse vendorSearchResponse = new VendorSearchResponse();
        try {
            vendorSearchResponse = populateVendorSearchResponse(gtin);
        } catch (Exception e) {
            log.error("Exception in Kwikee Search Service: " + e.toString());
        }
        return vendorSearchResponse;
    }


    private VendorSearchResponse populateVendorSearchResponse(String gtin)  throws Exception{
        VendorSearchResponse response = new VendorSearchResponse();
        response.setDescription(getKwikeeDescription(gtin));
        response.setGtin(gtin);
        response.setImageType(imageType);
        response.setSource("KWIKEE");
        response.setBackground(background);
        response.setProvidedSize(providedSize);
        response.setViewAngleList(getKwikeeImages(gtin));
        return response;
    }

    private String getKwikeeDescription(String gtin) throws Exception{
        KwikeeAPI kwikeeAPI = new KwikeeAPI(properties);
        String kwikeeSearchRespJSON = kwikeeAPI.getItemDataJSON(gtin);
        JSONObject jsonAssetObject = new JSONObject(kwikeeSearchRespJSON);
        return jsonAssetObject.getJSONArray("customProductName").
                getJSONObject(0).getString("text").toString();
    }

    private List<VendorSearchViewAngleResponse> getKwikeeImages(String gtin) throws Exception{
        KwikeeAPI kwikeeAPI = new KwikeeAPI(properties);
        return vendorUtil.getViewAngleList(kwikeeAPI.imageSearch(gtin));
    }

}
