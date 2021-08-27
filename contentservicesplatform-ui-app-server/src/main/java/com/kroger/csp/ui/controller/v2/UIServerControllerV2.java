package com.kroger.csp.ui.controller.v2;

import com.kroger.csp.ui.converter.v2.AddImageV2RequestConverter;
import com.kroger.csp.ui.converter.v2.SearchImageV2RequestConvertor;
import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.v2.SearchImageV2APIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import com.kroger.csp.ui.service.v2.AddImageV2Service;
import com.kroger.csp.ui.service.v2.SearchImageV2Service;
import com.kroger.csp.ui.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private SearchImageV2Service searchImageV2Service;
    @Autowired
    private SearchImageV2RequestConvertor searchImageV2RequestConverter;
    @Autowired
    private CommonUtils utils;

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
            //TODO: Handle UI server specific exceptions
            e.printStackTrace();
            log.error("Error in V2 Csp Add - UI : " + e);
            addImageUIResponse.setErrorResponse(utils.populateErrorResponse(e));
        }
        return addImageUIResponse;
    }

    /**
     * Method for handling API call of /imp/ui/v2/server/cspSearch
     * This is hit CSP V2 search API and will get back the list of GTIN.
     * At least one of the parameters between imageID and GTIN must be present to perform the search.
     *
     * @param imageId     Image ID for which the User wants to search
     * @param gtin        GTIN for which the User wants to search
     * @param referenceId Reference ID which UI will send to CSP API while submitting the http request
     * @return List of Images associated with te search criteria.
     */
    @GetMapping(value = "/cspSearch")
    public SearchResponse searchImageInCSP(@RequestParam(value = "imageId", required = false) String imageId,
                                           @RequestParam(value = "gtin", required = false) String gtin,
                                           @RequestParam(value = "referenceId", required = true) String referenceId,
                                           Authentication auth) {
        SearchResponse response = new SearchResponse();
        try {
            SearchImageV2APIRequest searchImageV2APIRequest = searchImageV2RequestConverter.populateAPIRequest(gtin, imageId, referenceId);
            response = searchImageV2Service.searchImages(searchImageV2APIRequest);
        } catch (Exception e) {
            //TODO: Handle UI server specific exceptions
            e.printStackTrace();
            log.error("Error in V2 Csp Search - UI : " + e);
            response.setError(utils.populateErrorDetails(e));
        }
        return response;
    }

}
