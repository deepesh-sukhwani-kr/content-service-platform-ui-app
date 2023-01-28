package com.kroger.csp.ui;

import com.kroger.csp.ui.controller.VendorSearchController;
import com.kroger.csp.ui.service.SyndigoSearchService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"local", "SPOOFING"})
public class ContentServicesPlatformUiAppApplicationTests {

	@Autowired
	private VendorSearchController controller;
	@Autowired
	private SyndigoSearchService syndigoService;


	@Test
	public void contextLoads() {
		controller.vendorSearch("syndigo","00011110028754");
	}

	//@Ignore
	@Test
	public void testChecksImageSearchReturnsSyndigoImageAttributesWithAnotherGtin() throws Exception {

		//Then
		Assert.assertNotNull(syndigoService.getImageDetailsByGtin("00020000447117"));
	}
}
