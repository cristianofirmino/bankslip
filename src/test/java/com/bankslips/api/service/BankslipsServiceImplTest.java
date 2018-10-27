package com.bankslips.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.bankslips.api.util.CalcsUtil;
import com.bankslips.api.util.DataTestUtil;
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

	private static final Logger log = LoggerFactory.getLogger(BankslipsServiceImpl.class);

	@Autowired
	private BankslipstRepository repository;

	@Autowired
	private BankslipsServiceImpl service;

	@Autowired
	ParseBankspliEntityDTO parse;

	private int size;
	private int daysAgo;
	private int entitiesSizeInDatabase;
	private String customer;
	private Date date;
	private Date paymentDate;
	private BigDecimal totalInCents;

	@Before
	public void setup() throws Exception {

		size = 10;
		entitiesSizeInDatabase = 22;
		daysAgo = -11;
		customer = "BankslipsServiceImplTest";
		date = new Date();
		totalInCents = BigDecimal.valueOf(1000.97);

		/*
		 * Plus one day in date for paymentDate
		 */
		paymentDate = DatesUtil.plusDays(date, 1);

		IntStream.rangeClosed(daysAgo, size).forEach(i -> {
			this.repository.save(DatabaseMockUtility.newBankslip(customer + i, i));
		});

	}

	@After
	public final void tearDown() {
		this.repository.deleteAll();
	}

	@Test
	public void testPersistDTOIsPresent() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);

		assertTrue(entityPersisted.isPresent());
		assertTrue(dtoPersisted.isPresent());

	}

	@Test
	public void testDTOPersistedHasStatusPending() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);

		assertEquals(entityPersisted.get().getStatus(), StatusEnum.PENDING);
		assertEquals(((BankslipDTO) dtoPersisted.get()).getStatus(), StatusEnum.PENDING);

	}

	@Test
	public void testeIfPersistedEntityHasCreationAndUpdateDate() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);

		assertNotNull(entityPersisted.get().getCreated());
		assertNotNull(entityPersisted.get().getUpdated());
	}

	@Test
	public void testIfSomeAttributesOfDTOAndPersistedEntityAreEquals() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);
		BankslipDTO bankslipDTO = DataTestUtil.getBankslipDTO(dto);

		assertEquals(bankslipDTO.getCustomer(), entityPersisted.get().getCustomer());
		assertEquals(bankslipDTO.getTotalInCents(), entityPersisted.get().getTotalInCents());
		assertEquals(bankslipDTO.getDueDate(), entityPersisted.get().getDueDate());
	}

	@Test
	public void testAPersistedDTOAndPersistedEntityAreaEquals() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);
		BankslipDTO bankslipDTOPersisted = DataTestUtil.getBankSlipDTO(dtoPersisted);

		assertEquals(bankslipDTOPersisted.getId().get(), entityPersisted.get().getId());
		assertEquals(bankslipDTOPersisted.getCustomer(), entityPersisted.get().getCustomer());
		assertEquals(bankslipDTOPersisted.getTotalInCents(), entityPersisted.get().getTotalInCents());
		assertEquals(bankslipDTOPersisted.getDueDate(), entityPersisted.get().getDueDate());
		assertEquals(bankslipDTOPersisted.getStatus(), entityPersisted.get().getStatus());
	}

	@Test
	public void testSomeAttributesOfADTOAndPersistedDTOAreEquals() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		BankslipDTO bankslipDTO = DataTestUtil.getBankslipDTO(dto);
		BankslipDTO bankslipDTOPersisted = DataTestUtil.getBankSlipDTO(dtoPersisted);

		assertEquals(bankslipDTO.getCustomer(), bankslipDTOPersisted.getCustomer());
		assertEquals(bankslipDTO.getTotalInCents(), bankslipDTOPersisted.getTotalInCents());
		assertEquals(bankslipDTO.getDueDate(), bankslipDTOPersisted.getDueDate());
	}

	@Test
	public void testDTOFoundDueGreater10DaysIsPresent() {
		String id = getIdFindCustomer(daysAgo);
		Optional<DTO> dtoFoundDueGreater10Days = DataTestUtil.getDTOById(service, id);

		assertTrue(dtoFoundDueGreater10Days.isPresent());
		assertEquals(dtoFoundDueGreater10Days.get().getId().get(), id);
	}

	@Test
	public void testFineOfBankslipDueGreaterThan10DaysIsCorrect() {
		String id = getIdFindCustomer(daysAgo);
		Optional<DTO> dtoFoundDueGreater10Days = DataTestUtil.getDTOById(service, id);
		BankslipDTO dtoDueGreater10Days = (BankslipDTO) dtoFoundDueGreater10Days.get();
		Optional<BigDecimal> fine = CalcsUtil.calcFine(dtoDueGreater10Days.getDueDate(),
				dtoDueGreater10Days.getTotalInCents());

		assertEquals(dtoDueGreater10Days.getFine(), fine.get());
		assertTrue(dtoDueGreater10Days.getFine().doubleValue() > 0);

		log.info("DTO Fine Greater 10 Days: " + dtoDueGreater10Days);
		log.info("Fine Greater 10 Days: " + fine.get());
	}

	@Test
	public void testDTOFoundDueLess10DaysIsPresent() {
		String id = getIdFindCustomer(daysAgo + 1);
		Optional<DTO> dtoFoundDueLess10Days = DataTestUtil.getDTOById(service, id);

		assertTrue(dtoFoundDueLess10Days.isPresent());
		assertEquals(dtoFoundDueLess10Days.get().getId().get(), id);
	}

	@Test
	public void testFineOfBankslipDueLess10DaysIsCorrect() {
		String id = getIdFindCustomer(daysAgo + 1);
		Optional<DTO> dtoFoundDueLess10Days = DataTestUtil.getDTOById(service, id);
		BankslipDTO dtoDueLess10Days = DataTestUtil.getBankSlipDTO(dtoFoundDueLess10Days);
		Optional<BigDecimal> fineLess = CalcsUtil.calcFine(dtoDueLess10Days.getDueDate(),
				dtoDueLess10Days.getTotalInCents());

		assertEquals(dtoDueLess10Days.getFine(), fineLess.get());
		assertTrue(dtoDueLess10Days.getFine().doubleValue() > 0);

		log.info("DTO Fine Less 10 Days: " + dtoDueLess10Days);
		log.info("Fine Less 10 Days: " + fineLess.get());
	}

	@Test
	public void testDueDateAndDaysAgoGreaterAreEquals() {
		String id = getIdFindCustomer(daysAgo);
		Optional<DTO> dtoFoundDueGreater10Days = DataTestUtil.getDTOById(service, id);
		BankslipDTO dtoDueGreater10Days = DataTestUtil.getBankSlipDTO(dtoFoundDueGreater10Days);

		String daysAgoDateGreater = DatesUtil.simpleFormaDate(DatesUtil.plusDays(this.date, daysAgo));
		String dueDateGreater = DatesUtil.simpleFormaDate(dtoDueGreater10Days.getDueDate());
		assertEquals(daysAgoDateGreater, dueDateGreater);
	}

	@Test
	public void testDueDateAndDaysAgoLessAreEquals() {
		String id = getIdFindCustomer(daysAgo + 1);
		Optional<DTO> dtoFoundDueLess10Days = DataTestUtil.getDTOById(service, id);
		BankslipDTO dtoDueLess10Days = DataTestUtil.getBankSlipDTO(dtoFoundDueLess10Days);
		String daysAgoDateLess = DatesUtil.simpleFormaDate(DatesUtil.plusDays(this.date, (daysAgo + 1)));
		String dueDateLess = DatesUtil.simpleFormaDate(dtoDueLess10Days.getDueDate());

		assertEquals(daysAgoDateLess, dueDateLess);

	}
	
	@Test
	public void testDTONotFound() {
		String id = UUID.randomUUID().toString();
		Optional<DTO> dtoFoundDueGreater10Days = DataTestUtil.getDTOById(service, id);

		assertFalse(dtoFoundDueGreater10Days.isPresent());
	}

	@Test
	public void testfindAll() {
		List<DTO> listDTO = service.findAll();

		listDTO.forEach(dto -> log.info(dto.toString()));

		assertNotNull(listDTO);
		assertTrue(listDTO.size() > 0);
		assertTrue(listDTO.size() == entitiesSizeInDatabase);
	}

	@Test
	public void testePay() {
		Optional<DTO> optionalDTO = DataTestUtil.getOptionalDTO(service, date, customer, totalInCents);
		String id = getIdFromOptionalDTO(optionalDTO);
		boolean wasPay = service.pay(optionalDTO, paymentDate);
		BankslipDTO dtoPersistedAndPaid = getBankslipDTO(id);

		assertTrue(wasPay);
		assertNotEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PENDING);
		assertEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PAID);
	}

	@Test
	public void testCancel() {
		Optional<DTO> optionalDTO = DataTestUtil.getOptionalDTO(service, date, customer, totalInCents);
		String id = getIdFromOptionalDTO(optionalDTO);
		boolean wasCancel = service.cancel(id);
		BankslipDTO dtoPersistedAndPaid = getBankslipDTO(id);

		assertTrue(wasCancel);
		assertNotEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PENDING);
		assertEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.CANCELED);
	}
	
	@Test
	public void testCancelNotFound() {
		String id = UUID.randomUUID().toString();
		boolean wasCancel = service.cancel(id);

		assertFalse(wasCancel);
	}

	private BankslipDTO getBankslipDTO(String id) {
		BankslipDTO dtoPersistedAndPaid = (BankslipDTO) service.findById(id).get();
		return dtoPersistedAndPaid;
	}

	private String getIdFromOptionalDTO(Optional<DTO> optionalDTO) {
		String id = optionalDTO.get().getId().get();
		return id;
	}

	private String getIdFindCustomer(int daysAgo) {
		String idDue = this.repository.findByCustomer(customer + daysAgo).get().getId();
		return idDue;
	}

}
