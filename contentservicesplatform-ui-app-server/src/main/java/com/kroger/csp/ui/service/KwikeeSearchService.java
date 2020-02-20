package com.kroger.csp.ui.service;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import com.kroger.csp.ui.util.VendorUtil;
import com.kroger.imp.apm.KwikeeAPI;
import com.kroger.imp.apm.KwikeeImageAttributes;
import com.kroger.imp.apm.KwikeeViewAngleMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Service class to support Kwikee vendor search
 */
@Service
@Slf4j
public class KwikeeSearchService {

    @Autowired
    private VendorUtil vendorUtil;
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

    /**
     * @param gtin
     * @return
     * @throws Exception
     */
    public VendorSearchResponse getImageDetailsByGtin(String gtin) throws Exception {

        KwikeeAPI api = new KwikeeAPI(properties);
        KwikeeImageAttributes attributes = api.imageSearchReturnsKwikeeImageAttributes(gtin);

        VendorSearchResponse response = new VendorSearchResponse();
        response.setDescription(attributes.getDescription());
        response.setGtin(gtin);
        response.setImageType(imageType);
        response.setSource(KwikeeConstants.KWIKEE.value);
        response.setBackground(background);
        response.setProvidedSize(providedSize);
        response.setViewAngleList(getKwikeeImages(attributes));
        return response;
    }

    /**
     * @param attributes
     * @return
     * @throws Exception
     */
    private List<VendorSearchViewAngleResponse> getKwikeeImages(KwikeeImageAttributes attributes){
        Map<String, KwikeeViewAngleMap> searchResults = attributes.getViewAngleMap();
        List<VendorSearchViewAngleResponse> viewAngles = new ArrayList<>();
        for(String viewAngle: searchResults.keySet()){
            if(vendorUtil.isSupportedViewAngle(viewAngle)) {
                VendorSearchViewAngleResponse vendorSearchViewAngleResponse = new VendorSearchViewAngleResponse();
                vendorSearchViewAngleResponse.setViewAngle(viewAngle);
                vendorSearchViewAngleResponse.setUrl(searchResults.get(viewAngle).getImageUrl());
                String timeStamp = searchResults.get(viewAngle).getImageLastModifiedDate();
                if(StringUtils.isNotBlank(timeStamp) && timeStamp.trim().length() == 20){
                    timeStamp = timeStamp.substring(0, 18)+ ".000Z";
                }
                vendorSearchViewAngleResponse.setLastModifiedDate(timeStamp);
                viewAngles.add(vendorSearchViewAngleResponse);
            }
        }
        return viewAngles;
    }


    public byte[] getRawImage(String url) throws Exception{
        return Base64.decodeBase64(new KwikeeAPI(properties).getAssetFromURL(url));
    }



    @AllArgsConstructor
    public enum KwikeeConstants {
        KWIKEE("KWIKEE"),
        CUSTOM_PRODUCT_NAME("customProductName"),
        TEXT("text");

        private String value;
    }

}
