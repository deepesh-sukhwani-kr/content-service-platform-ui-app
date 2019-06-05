package com.kroger.csp.ui;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"unittest", "SPOOFING"})
public class ContentServicesPlatformUiAppApplicationTests {

	@Test
	public void contextLoads() {
	}

}
