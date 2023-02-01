package com.kroger.csp.ui.service;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import com.kroger.csp.ui.util.VendorUtil;
import com.kroger.imp.apm.GladsonUpdatedAPI;
import com.kroger.imp.apm.SyndigoAPI;
import com.kroger.imp.apm.model.ViewAngleMapping;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Service class to support Syndigo vendor search
 */
@Service
@Slf4j
@RefreshScope
public class SyndigoSearchService {

    @Autowired
    private VendorUtil vendorUtil;
    @Autowired
    @Qualifier("syndigoProperties")
    Properties properties;
    @Value("${kroger.imagedata.viewangles}")
    private String[] viewAngles;
    @Value("${kroger.imagedata.size.syndigo}")
    private String providedSize;
    @Value("${kroger.imagedata.background.syndigo}")
    private String background;
    @Value("${kroger.imagedata.imagetype.syndigo}")
    private String imageType;

    /**
     * @param gtin
     * @return
     * @throws Exception
     */
    public VendorSearchResponse getImageDetailsByGtin(String gtin) throws Exception {
        VendorSearchResponse response = new VendorSearchResponse();
        SyndigoAPI api = new SyndigoAPI(properties);
        response.setGtin(gtin);
        response.setImageType(imageType);
        response.setSource(SyndigoConstants.SYNDIGO.value);
        response.setBackground(background);
        response.setProvidedSize(providedSize);
        response.setViewAngleList(vendorUtil.getViewAngleList(api.imageSearchReturnsSyndigoImageAttribute(gtin)));
        return response;
    }

    /**
     * @param attributes
     * @return
     * @throws Exception
     */
    public byte[] getRawImage(String url) throws Exception{
        return Base64.decodeBase64(new SyndigoAPI(properties).getAssetFromURL(url));
    }



    @AllArgsConstructor
    public enum SyndigoConstants {
        SYNDIGO("SYNDIGO"),
        CUSTOM_PRODUCT_NAME("customProductName"),
        TEXT("text");

        private String value;
    }

}
