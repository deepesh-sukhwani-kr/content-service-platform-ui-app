package com.kroger.csp.ui;

import com.kroger.csp.ui.controller.VendorSearchController;
import com.kroger.csp.ui.service.SyndigoSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles ({"local", "SPOOFING"})
public class ContentServicesPlatformUiAppApplicationTests {

    @Autowired
    private VendorSearchController controller;
    @Autowired
    private SyndigoSearchService syndigoService;

    @Test
    public void contextLoads() {
        controller.vendorSearch("syndigo", "00011110028754");
    }

    //@Ignore
    @Test
    public void testChecksImageSearchReturnsSyndigoImageAttributesWithAnotherGtin() throws Exception {

        //Then
        assertNotNull(syndigoService.getImageDetailsByGtin("00020000447117"));
    }
}
