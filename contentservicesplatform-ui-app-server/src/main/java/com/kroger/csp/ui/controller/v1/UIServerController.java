package com.kroger.csp.ui.controller.v1;

import com.kroger.csp.ui.converter.v1.AddImageRequestConverter;
import com.kroger.csp.ui.converter.v1.SearchImageRequestConverter;
import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import com.kroger.csp.ui.service.v1.AddImageService;
import com.kroger.csp.ui.service.v1.SearchImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for UI server to hit CSP V1 API's
 */
@RestController
@RequestMapping(value = "/imp/ui/v1/server", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UIServerController {

    @Autowired
    private AddImageService addImageService;
    @Autowired
    private AddImageRequestConverter addImageRequestConverter;
    @Autowired
    private SearchImageRequestConverter searchImageRequestConverter;
    @Autowired
    private SearchImageService searchImageService;

    /**
     * Method for handling API call of /imp/ui/v1/server/addImage
     * This is hit CSP V1 add API and will try to add the submitted image(s) into CSP.
     * @param request The image(s) and the corresponding metadata
     * @return Status of the CSP add
     */
    @PostMapping(value = "/addImage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddImageUIResponse addImage(@RequestBody AddImageUIRequest request) {
        AddImageUIResponse addImageUIResponse = new AddImageUIResponse();
        try {
            AddImageAPIRequest addImageAPIRequest = addImageRequestConverter.populateAPIRequest(request);
            addImageUIResponse = addImageService.addImage(addImageAPIRequest);
        } catch (Exception e) {
            log.error("Error in V1 Csp Add - UI : " + e);
        }
        return addImageUIResponse;
    }

    /**
     * Method for handeling API call of /imp/ui/v1/server/cspSearch
     * This is hit CSP V1 search API and will get back the list of GTIN.
     * At least one of the parameters between imageID and GTIN must be present to perform the search.
     * @param imageId Image ID for which the User wants to search
     * @param gtin GTIN for which the User wants to search
     * @param referenceId Reference ID which UI will send to CSP API while submitting the http request
     * @return List of Images associated with te search criteria.
     */
    @GetMapping(value = "/cspSearch")
    public SearchResponse searchImageInCSP(@RequestParam(value = "imageId", required = false) String imageId,
                                           @RequestParam(value = "gtin", required = false) String gtin,
                                           @RequestParam(value = "referenceId", required = true) String referenceId) {
        SearchResponse response = new SearchResponse();
        try {
            SearchImageAPIRequest searchImageAPIRequest = searchImageRequestConverter.populateAPIRequest(gtin, imageId, referenceId);
            response = searchImageService.searchImage(searchImageAPIRequest);
        } catch (Exception e) {
            log.error("Error in V1 Csp Search - UI : " + e);
        }
        return response;
    }
}
