package com.bankslips.api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.bankslips.api.entity.BankslipEntity;
import com.bankslips.api.util.PopulateDatabaseUtility;

/**
 * Test Class BanslipRepository
 * 
 * @author Cristiano Firmino
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BankslipRepositoryTest {

	@Autowired
	private BankslipstRepository repository;

	@Before
	public void setup() throws Exception {

		IntStream.rangeClosed(1, 10).forEach(i -> {
			this.repository.save(PopulateDatabaseUtility.newBankslip("Agent 00" + i));
		});

	}

	@After
	public final void tearDown() {
		this.repository.deleteAll();
	}

	@Test
	public void testGetAllBanksplits() {
		Iterable<BankslipEntity> allBanksplits = this.repository.findAll();
		assertNotNull(allBanksplits);
		assertEquals(10, allBanksplits.spliterator().getExactSizeIfKnown());
	}

	@Test
	public void testFindByCustomer() {
		String customer = "Agent 007";
		Optional<BankslipEntity> opBankslip = this.repository.findByCustomer(customer);

		assertTrue(opBankslip.isPresent());
		assertTrue(opBankslip.get() instanceof BankslipEntity);
		assertEquals(customer, opBankslip.get().getCustomer());
	}

	@Test
	public void testFindById() {
		String id = this.repository.findByCustomer("Agent 007").get().getId();
		Optional<BankslipEntity> opBankslip = this.repository.findById(id);
		BankslipEntity bankslip = (BankslipEntity) opBankslip.get();

		assertEquals(id, bankslip.getId());
	}

}
