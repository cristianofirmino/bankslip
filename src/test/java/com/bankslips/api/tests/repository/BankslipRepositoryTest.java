package com.bankslips.api.tests.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import com.bankslips.api.BankslipsApplication;
import com.bankslips.api.entity.BankslipEntity;
import com.bankslips.api.repository.BankslipstRepository;
import com.bankslips.api.tests.util.DatabaseMockUtility;

/**
 * Test Class BanslipRepository
 * 
 * @author Cristiano Firmino
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankslipsApplication.class)
@ActiveProfiles("test")
public class BankslipRepositoryTest {

	@Autowired
	private BankslipstRepository repository;

	private int size;
	private String customer;

	@Before
	public void setup() throws Exception {

		size = 10;
		customer = "BankslipstRepository";

		IntStream.rangeClosed(1, size).forEach(i -> {
			this.repository.save(DatabaseMockUtility.newBankslip(customer + i, i));
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
		assertTrue(allBanksplits.spliterator().getExactSizeIfKnown() > 0);
		assertEquals(size, allBanksplits.spliterator().getExactSizeIfKnown());
	}

	@Test
	public void testFindByCustomer() {
		Optional<BankslipEntity> opBankslip = this.repository.findByCustomer(customer + 7);

		assertTrue(opBankslip.isPresent());
		assertTrue(opBankslip.get() instanceof BankslipEntity);
		assertEquals(customer + 7, opBankslip.get().getCustomer());
	}

	@Test
	public void testFindById() {
		String id = this.repository.findByCustomer(customer + 7).get().getId();
		Optional<BankslipEntity> opBankslip = this.repository.findById(id);
		BankslipEntity bankslip = (BankslipEntity) opBankslip.get();

		assertEquals(id, bankslip.getId());
	}

	@Test
	public void testDeleteById() {
		String id = this.repository.findByCustomer(customer + 7).get().getId();
		this.repository.deleteById(id);
		Optional<BankslipEntity> entity = this.repository.findById(id);

		assertFalse(entity.isPresent());
	}

}
