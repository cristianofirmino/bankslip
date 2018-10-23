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

import com.bankslips.api.entity.Bankslip;
import com.bankslips.api.repository.Repository;
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
	private Repository banksplitRepository;

	@Before
	public void setup() throws Exception {

		IntStream.rangeClosed(1, 10).forEach(i -> {
			this.banksplitRepository.save(
					PopulateDatabaseUtility.newBankslip("Agent 00" + i));
		});

	}

	@After
	public final void tearDown() {
		this.banksplitRepository.deleteAll();
	}

	@Test
	public void testGetAllBanksplits() {
		Iterable<Bankslip> allBanksplits = this.banksplitRepository.findAll();
		assertNotNull(allBanksplits);
		assertEquals(10, allBanksplits.spliterator().getExactSizeIfKnown());
	}

	@Test
	public void testFindByCustomer() {
		String customer = "Agent 007";
		Optional<Bankslip> opBankslip = this.banksplitRepository.findByCustomer(customer);

		assertTrue(opBankslip.isPresent());
		assertTrue(opBankslip.get() instanceof Bankslip);
		assertEquals(customer, opBankslip.get().getCustomer());
	}

	@Test
	public void testFindById() {
		String id = this.banksplitRepository.findByCustomer("Agent 007").get().getId();
		Optional<Bankslip> opBankslip = this.banksplitRepository.findById(id);
		Bankslip bankslip = opBankslip.get();

		assertEquals(id, bankslip.getId());
	}

}
