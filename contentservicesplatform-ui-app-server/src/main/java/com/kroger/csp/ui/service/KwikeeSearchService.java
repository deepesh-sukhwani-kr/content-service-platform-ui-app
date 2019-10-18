package com.kroger.csp.ui.service;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import com.kroger.csp.ui.util.VendorUtil;
import com.kroger.imp.apm.KwikeeAPI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 *
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
        VendorSearchResponse response = new VendorSearchResponse();
        response.setDescription(getKwikeeDescription(gtin));
        response.setGtin(gtin);
        response.setImageType(imageType);
        response.setSource(KwikeeConstants.KWIKEE.value);
        response.setBackground(background);
        response.setProvidedSize(providedSize);
        response.setViewAngleList(getKwikeeImages(gtin));
        return response;
    }

    /**
     * @param gtin
     * @return
     * @throws Exception
     */
    private String getKwikeeDescription(String gtin) throws Exception {
        KwikeeAPI kwikeeAPI = new KwikeeAPI(properties);
        String kwikeeSearchRespJSON = kwikeeAPI.getItemDataJSON(gtin);
        JSONObject jsonAssetObject = new JSONObject(kwikeeSearchRespJSON);
        return jsonAssetObject.getJSONArray(KwikeeConstants.CUSTOM_PRODUCT_NAME.value).
                getJSONObject(0).getString(KwikeeConstants.TEXT.value);
    }

    /**
     * @param gtin
     * @return
     * @throws Exception
     */
    private List<VendorSearchViewAngleResponse> getKwikeeImages(String gtin) throws Exception {
        KwikeeAPI kwikeeAPI = new KwikeeAPI(properties);
        return vendorUtil.getViewAngleList(kwikeeAPI.imageSearch(gtin));
    }

    @AllArgsConstructor
    public enum KwikeeConstants {
        KWIKEE("KWIKEE"),
        CUSTOM_PRODUCT_NAME("customProductName"),
        TEXT("text");

        private String value;
    }

}
