package com.kroger.csp.ui;

import com.kroger.csp.ui.controller.VendorSearchController;
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


	@Test
	public void contextLoads() {
		controller.vendorSearch("kwikee","00011110028754");
	}

}
