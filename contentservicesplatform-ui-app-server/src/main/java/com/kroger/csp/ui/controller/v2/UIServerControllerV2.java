package com.kroger.csp.ui.controller.v2;

import com.kroger.csp.ui.converter.v2.AddImageV2RequestConverter;
import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.service.v2.AddImageV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for UI server to hit CSP V2 API's
 */
@RestController
@RequestMapping(value = "/imp/ui/v2/server", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UIServerControllerV2 {

    @Autowired
    private AddImageV2Service addImageV2Service;
    @Autowired
    private AddImageV2RequestConverter addImageV2RequestConverter;

    /**
     * Method for handling API call of /imp/ui/v1/server/addImage
     * This is hit CSP V2 add API and will try to add the submitted image(s) into CSP.
     * @param request The image(s) and the corresponding metadata
     * @return Status of the CSP add
     */
    @PostMapping(value = "/addImage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddImageUIResponse addImageV2(@RequestBody AddImageUIRequest request) {
        AddImageUIResponse addImageUIResponse = new AddImageUIResponse();
        try {
            addImageUIResponse = addImageV2Service.addImage(addImageV2RequestConverter.populateAPIRequest(request));
        } catch (Exception e) {
            log.error("Error in V2 Csp Add - UI : " + e);
        }
        return addImageUIResponse;
    }

}
