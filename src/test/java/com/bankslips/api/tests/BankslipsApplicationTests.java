package com.bankslips.api.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.bankslips.api.BankslipsApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankslipsApplication.class)
@ActiveProfiles("test")
public class BankslipsApplicationTests {

	@Test
	public void contextLoads() {
	}

}
