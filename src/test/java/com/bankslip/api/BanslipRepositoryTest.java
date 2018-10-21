package com.bankslip.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
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

import com.bankslip.api.entity.Bankslip;
import com.bankslip.api.enums.StatusEnum;
import com.bankslip.api.repository.BanksplitRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BanslipRepositoryTest {

	@Autowired
	private BanksplitRepository banksplitRepository;

	@Before
	public void setup() throws Exception {

		IntStream.rangeClosed(1, 10).forEach(i -> {
			this.banksplitRepository.save(getNewBankslip("Agent 00" + i));
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

	private Bankslip getNewBankslip(String customer) {
		Bankslip bankslip = new Bankslip();
		Date date = new Date();
		bankslip.setCustomer(customer);
		bankslip.setDueDate(getDueDate(date));
		bankslip.setStatus(StatusEnum.PENDING);
		bankslip.setTotalInCents(new BigDecimal(Math.random() * 1000));
		return bankslip;
	}

	private Date getDueDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 10);
		Date dueDate = c.getTime();

		return dueDate;
	}

	public static void main(String[] args) {

	}

}
