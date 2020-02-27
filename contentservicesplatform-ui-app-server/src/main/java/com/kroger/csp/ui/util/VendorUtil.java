package com.kroger.csp.ui.util;

import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Dhriti Ghosh
 */
@Component
@RefreshScope
public class VendorUtil {

    @Value("${kroger.imagedata.viewangles}")
    private String[] viewAngles;

    public boolean isSupportedViewAngle(String viewAngle){
        return Arrays.asList(this.viewAngles).stream()
                .anyMatch(angle -> StringUtils.isNotBlank(viewAngle) && angle.equalsIgnoreCase(viewAngle.trim()));
    }

    public List<VendorSearchViewAngleResponse> getViewAngleList
            (Map<String, String> searchResults){
        List<VendorSearchViewAngleResponse> viewAngles = new ArrayList<>();
        for(String viewAngle: searchResults.keySet()){
            if(isSupportedViewAngle(viewAngle)) {
                VendorSearchViewAngleResponse vendorSearchViewAngleResponse = new VendorSearchViewAngleResponse();
                vendorSearchViewAngleResponse.setViewAngle(viewAngle);
                vendorSearchViewAngleResponse.setUrl(searchResults.get(viewAngle));
                viewAngles.add(vendorSearchViewAngleResponse);
            }
        }
        return viewAngles;
    }
}
