package com.kroger.csp.ui.controller.v2;/*
package com.kroger.csp.ui.controller.v2;

import com.kroger.csp.ui.converter.v2.AddImageV2RequestConverter;
import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.v2.AddImageV2APIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.service.v2.AddImageV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imp/ui/v2/server")
public class UIServerControllerV2 {

    @Autowired
    private AddImageV2Service addImageV2Service;

    @Autowired
    private AddImageV2RequestConverter addImageV2RequestConverter;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AddImageUIResponse addImageV2(@RequestBody AddImageUIRequest request) {

        AddImageUIResponse addImageUIResponse = new AddImageUIResponse();
        try {
            AddImageV2APIRequest addImageV2APIRequest = addImageV2RequestConverter.populateAPIRequest(request);

            addImageUIResponse = addImageV2Service.addImage(addImageV2APIRequest);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return addImageUIResponse;
    }

}
*/
