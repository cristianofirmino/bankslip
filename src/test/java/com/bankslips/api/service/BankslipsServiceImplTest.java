package com.bankslips.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.BankslipEntity;
import com.bankslips.api.enums.StatusEnum;
import com.bankslips.api.parse.ParseBankspliEntityDTO;
import com.bankslips.api.repository.BankslipstRepository;
import com.bankslips.api.util.DatabaseMockUtility;
import com.bankslips.api.util.DatesUtil;

/**
 * Test Class BankslipsServiceImpl
 * 
 * @author Cristiano Firmino
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BankslipsServiceImplTest {

	@Autowired
	private BankslipstRepository repository;

	@Autowired
	private BankslipsServiceImpl service;

	@Autowired
	ParseBankspliEntityDTO parse;

	private int size;
	private int daysAgo;
	private int entitiesSizeInDatabase;
	private String consumer;
	private Date date;
	private Date paymentDate;
	private BigDecimal totalInCents;

	@Before
	public void setup() throws Exception {

		size = 10;
		entitiesSizeInDatabase = 22;
		daysAgo = -11;
		consumer = "BankslipsServiceImplTest";
		date = new Date();
		totalInCents = BigDecimal.valueOf(1000.97);

		/*
		 * Plus one day in date for paymentDate
		 */
		paymentDate = DatesUtil.plusDays(date, 1);

		IntStream.rangeClosed(daysAgo, size).forEach(i -> {
			this.repository.save(DatabaseMockUtility.newBankslip(consumer + i, i));
		});

	}

	@After
	public final void tearDown() {
		this.repository.deleteAll();
	}

	@Test
	public void testPersist() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, consumer, totalInCents);
		Optional<DTO> dtoPersisted = service.persist(dto);
		Optional<BankslipEntity> entityPersisted = this.repository.findById(dtoPersisted.get().getId().get());

		assertTrue(entityPersisted.isPresent());
		assertTrue(dtoPersisted.isPresent());

		/*
		 * Check if DTO that was persisted has status PENDING
		 */
		assertEquals(entityPersisted.get().getStatus(), StatusEnum.PENDING);
		assertEquals(((BankslipDTO) dtoPersisted.get()).getStatus(), StatusEnum.PENDING);

		/*
		 * Check if a persisted entity has a creation and update date.
		 */
		assertNotNull(entityPersisted.get().getCreated());
		assertNotNull(entityPersisted.get().getUpdated());

		/*
		 * Check if some attributes of the DTO and persisted Entity are equals
		 */
		BankslipDTO bankslipDTO = (BankslipDTO) dto;
		assertEquals(bankslipDTO.getCustomer(), entityPersisted.get().getCustomer());
		assertEquals(bankslipDTO.getTotalInCents(), entityPersisted.get().getTotalInCents());
		assertEquals(bankslipDTO.getDueDate(), entityPersisted.get().getDueDate());

		/*
		 * Check if a persisted DTO and persisted Entity area equals
		 */
		BankslipDTO bankslipDTOPersisted = (BankslipDTO) dtoPersisted.get();
		assertEquals(bankslipDTOPersisted.getId().get(), entityPersisted.get().getId());
		assertEquals(bankslipDTOPersisted.getCustomer(), entityPersisted.get().getCustomer());
		assertEquals(bankslipDTOPersisted.getTotalInCents(), entityPersisted.get().getTotalInCents());
		assertEquals(bankslipDTOPersisted.getDueDate(), entityPersisted.get().getDueDate());
		assertEquals(bankslipDTOPersisted.getStatus(), entityPersisted.get().getStatus());

		/*
		 * Check if some attributes of the DTO and persisted DTO are equals
		 */
		assertEquals(bankslipDTO.getCustomer(), bankslipDTOPersisted.getCustomer());
		assertEquals(bankslipDTO.getTotalInCents(), bankslipDTOPersisted.getTotalInCents());
		assertEquals(bankslipDTO.getDueDate(), bankslipDTOPersisted.getDueDate());

	}

	@Test
	public void testFindById() {
		String id = this.repository.findByCustomer("BankslipsServiceImplTest" + daysAgo).get().getId();
		Optional<DTO> dtoFound = this.service.findById(id);

		assertTrue(dtoFound.isPresent());
		assertEquals(dtoFound.get().getId().get(), id);

		/*
		 * Check if the fine of the bankslip due is correct.
		 */
		BankslipDTO dtoDue = (BankslipDTO) dtoFound.get();
		System.out.println(dtoDue);
		assertTrue(dtoDue.getFine().doubleValue() > 0);

		/*
		 * Check due date and days ago are equals
		 */
		String daysAgoDate = DatesUtil.simpleFormaDate(DatesUtil.plusDays(this.date, daysAgo));
		String dueDate = DatesUtil.simpleFormaDate(dtoDue.getDueDate());
		assertEquals(daysAgoDate, dueDate);

	}

	@Test
	public void testfindAll() {
		List<DTO> listDTO = service.findAll();

		listDTO.forEach(System.out::println);

		assertNotNull(listDTO);
		assertTrue(listDTO.size() > 0);
		assertTrue(listDTO.size() == entitiesSizeInDatabase);
	}

	@Test
	public void testePay() {
		Optional<DTO> optionalDTO = getOptionalDTO();
		String id = optionalDTO.get().getId().get();
		boolean wasPay = service.pay(optionalDTO, paymentDate);
		BankslipDTO dtoPersistedAndPaid = (BankslipDTO) service.findById(id).get();

		assertTrue(wasPay);
		assertNotEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PENDING);
		assertEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PAID);
	}

	@Test
	public void cancel() {
		Optional<DTO> optionalDTO = getOptionalDTO();
		String id = optionalDTO.get().getId().get();
		service.cancel(id);
		BankslipDTO dtoPersistedAndPaid = (BankslipDTO) service.findById(id).get();

		assertNotEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PENDING);
		assertEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.CANCELED);
	}

	private Optional<DTO> getOptionalDTO() {
		return DatabaseMockUtility.getOptionalDTOPersisted(service, date, consumer, totalInCents);
	}

}
