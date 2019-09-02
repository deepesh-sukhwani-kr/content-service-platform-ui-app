package com.kroger.csp.ui.controller.v1;

import com.kroger.csp.ui.converter.v1.AddImageRequestConverter;
import com.kroger.csp.ui.converter.v1.SearchImageRequestConverter;
import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.SearchImageUIResponse;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import com.kroger.csp.ui.service.v1.AddImageService;
import com.kroger.csp.ui.service.v1.SearchImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/imp/ui/v1/server")
public class UIServerController {

    @Autowired
    private AddImageService addImageService;

    @Autowired
    private AddImageRequestConverter addImageRequestConverter;

    @Autowired
    private SearchImageRequestConverter searchImageRequestConverter;

    @Autowired
    private SearchImageService searchImageService;



    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/addImage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AddImageUIResponse addImage(@RequestBody AddImageUIRequest request) {

        AddImageUIResponse addImageUIResponse = new AddImageUIResponse();
        try {
            AddImageAPIRequest addImageAPIRequest = addImageRequestConverter.populateAPIRequest(request);

            addImageUIResponse = addImageService.addImage(addImageAPIRequest);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return addImageUIResponse;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/addMultipleImages", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AddImageUIResponse> addMultipleImages (@RequestBody List<AddImageUIRequest> requestList) {

        List<AddImageUIResponse> addImageUIResponseList = new ArrayList<>();

        try {
            for(AddImageUIRequest addImageUIRequest : requestList) {

                AddImageAPIRequest addImageAPIRequest = addImageRequestConverter.populateAPIRequest(addImageUIRequest);
                addImageUIResponseList.add(addImageService.addImage(addImageAPIRequest));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return addImageUIResponseList;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/cspSearch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResponse searchImageInCSP (@RequestParam(value = "imageId", required = false) String imageId,
                                            @RequestParam(value = "gtin", required = false) String gtin,
                                            @RequestParam(value = "referenceId", required = true) String referenceId) {

        SearchImageUIResponse searchImageUIResponse = new SearchImageUIResponse();
        SearchResponse response = new SearchResponse();
        try {
            SearchImageAPIRequest searchImageAPIRequest = searchImageRequestConverter.populateAPIRequest(gtin, imageId, referenceId);
            response = searchImageService.searchImage(searchImageAPIRequest);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return response;
    }
}
