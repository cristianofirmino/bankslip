package com.bankslips.api.tests.service;

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

import com.bankslips.api.BankslipsApplication;
import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.BankslipEntity;
import com.bankslips.api.enums.StatusEnum;
import com.bankslips.api.parse.ParseBanksplipEntityDTO;
import com.bankslips.api.repository.BankslipstRepository;
import com.bankslips.api.service.BankslipsServiceImpl;
import com.bankslips.api.tests.util.DataTestUtil;
import com.bankslips.api.tests.util.DatabaseMockUtility;
import com.bankslips.api.tests.util.DatesUtil;
import com.bankslips.api.util.CalcsUtil;

/**
 * Test Class BankslipsServiceImpl
 * 
 * @author Cristiano Firmino
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankslipsApplication.class)
@ActiveProfiles("test")
public class BankslipsServiceImplTest {

	private static final Logger log = LoggerFactory.getLogger(BankslipsServiceImpl.class);

	@Autowired
	private BankslipstRepository repository;

	@Autowired
	private BankslipsServiceImpl service;

	@Autowired
	ParseBanksplipEntityDTO parse;

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
	public void test_Persist_DTO_Is_Present() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);

		assertTrue(entityPersisted.isPresent());
		assertTrue(dtoPersisted.isPresent());

	}

	@Test
	public void test_DTO_Persisted_Has_Status_Pending() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);

		assertEquals(entityPersisted.get().getStatus(), StatusEnum.PENDING);
		assertEquals(((BankslipDTO) dtoPersisted.get()).getStatus(), StatusEnum.PENDING);

	}

	@Test
	public void teste_If_Persisted_Entity_Has_Creation_And_Update_Dates() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);

		assertNotNull(entityPersisted.get().getCreated());
		assertNotNull(entityPersisted.get().getUpdated());
	}

	@Test
	public void test_If_Some_Attributes_Of_DTO_And_Persisted_Entity_Are_Equals() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		Optional<BankslipEntity> entityPersisted = DataTestUtil.getOptionalBankslipEntity(dtoPersisted, repository);
		BankslipDTO bankslipDTO = DataTestUtil.getBankslipDTO(dto);

		assertEquals(bankslipDTO.getCustomer(), entityPersisted.get().getCustomer());
		assertEquals(bankslipDTO.getTotalInCents(), entityPersisted.get().getTotalInCents());
		assertEquals(bankslipDTO.getDueDate(), entityPersisted.get().getDueDate());
	}

	@Test
	public void test_A_Persisted_DTO_And_Persisted_Entity_Area_Equals() {
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
	public void test_Some_Attributes_Of_A_DTO_And_Persisted_DTO_Are_Equals() {
		DTO dto = DatabaseMockUtility.getOneDTO(date, customer, totalInCents);
		Optional<DTO> dtoPersisted = DataTestUtil.getOptionalDTO(service, dto);
		BankslipDTO bankslipDTO = DataTestUtil.getBankslipDTO(dto);
		BankslipDTO bankslipDTOPersisted = DataTestUtil.getBankSlipDTO(dtoPersisted);

		assertEquals(bankslipDTO.getCustomer(), bankslipDTOPersisted.getCustomer());
		assertEquals(bankslipDTO.getTotalInCents(), bankslipDTOPersisted.getTotalInCents());
		assertEquals(bankslipDTO.getDueDate(), bankslipDTOPersisted.getDueDate());
	}

	@Test
	public void test_DTO_Found_Due_Greater_10_Days_Is_Present() {
		String id = getIdFindCustomer(daysAgo);
		Optional<DTO> dtoFoundDueGreater10Days = DataTestUtil.getDTOById(service, id);

		assertTrue(dtoFoundDueGreater10Days.isPresent());
		assertEquals(dtoFoundDueGreater10Days.get().getId().get(), id);
	}

	@Test
	public void test_fine_of_bankslip_due_greater_than_10_days_is_correct() {
		String id = getIdFindCustomer(daysAgo);
		Optional<DTO> dtoFoundDueGreater10Days = DataTestUtil.getDTOById(service, id);
		BankslipDTO dtoDueGreater10Days = (BankslipDTO) CalcsUtil.calcFine(dtoFoundDueGreater10Days.get());

		assertTrue(dtoDueGreater10Days.getFine().doubleValue() > 0);
		assertTrue(((BankslipDTO) dtoFoundDueGreater10Days.get()).getFine().doubleValue() > 0);
		assertEquals(((BankslipDTO) dtoFoundDueGreater10Days.get()).getFine(), dtoDueGreater10Days.getFine());
	}

	@Test
	public void test_DTO_Found_Due_Less_10_Days_Is_Present() {
		String id = getIdFindCustomer(daysAgo + 1);
		Optional<DTO> dtoFoundDueLess10Days = DataTestUtil.getDTOById(service, id);

		assertTrue(dtoFoundDueLess10Days.isPresent());
		assertEquals(dtoFoundDueLess10Days.get().getId().get(), id);
	}

	@Test
	public void test_fine_of_bankslip_due_less_10_days_is_correct() {
		String id = getIdFindCustomer(daysAgo + 1);
		Optional<DTO> dtoFoundDueLess10Days = DataTestUtil.getDTOById(service, id);
		BankslipDTO dtoDueLess10Days = (BankslipDTO) CalcsUtil.calcFine(dtoFoundDueLess10Days.get());

		assertTrue(((BankslipDTO) dtoFoundDueLess10Days.get()).getFine().doubleValue() > 0);
		assertTrue(dtoDueLess10Days.getFine().doubleValue() > 0);
		assertEquals(((BankslipDTO) dtoFoundDueLess10Days.get()).getFine() , dtoDueLess10Days.getFine());
	}

	@Test
	public void test_Due_Date_And_Days_Ago_Greater_Are_Equals() {
		String id = getIdFindCustomer(daysAgo);
		Optional<DTO> dtoFoundDueGreater10Days = DataTestUtil.getDTOById(service, id);
		BankslipDTO dtoDueGreater10Days = DataTestUtil.getBankSlipDTO(dtoFoundDueGreater10Days);

		String daysAgoDateGreater = DatesUtil.simpleDate(DatesUtil.plusDays(this.date, daysAgo));
		String dueDateGreater = DatesUtil.simpleDate(dtoDueGreater10Days.getDueDate());
		assertEquals(daysAgoDateGreater, dueDateGreater);
	}

	@Test
	public void test_Due_Date_And_Days_Ago_Less_Are_Equals() {
		String id = getIdFindCustomer(daysAgo + 1);
		Optional<DTO> dtoFoundDueLess10Days = DataTestUtil.getDTOById(service, id);
		BankslipDTO dtoDueLess10Days = DataTestUtil.getBankSlipDTO(dtoFoundDueLess10Days);
		String daysAgoDateLess = DatesUtil.simpleDate(DatesUtil.plusDays(this.date, (daysAgo + 1)));
		String dueDateLess = DatesUtil.simpleDate(dtoDueLess10Days.getDueDate());

		assertEquals(daysAgoDateLess, dueDateLess);

	}
	
	@Test
	public void test_DTO_Not_Found() {
		String id = UUID.randomUUID().toString();
		Optional<DTO> dtoFoundDueGreater10Days = DataTestUtil.getDTOById(service, id);

		assertFalse(dtoFoundDueGreater10Days.isPresent());
	}

	@Test
	public void test_find_All() {
		List<DTO> listDTO = service.findAll();

		listDTO.forEach(dto -> log.info(dto.toString()));

		assertNotNull(listDTO);
		assertTrue(listDTO.size() > 0);
		assertTrue(listDTO.size() == entitiesSizeInDatabase);
	}

	@Test
	public void teste_make_a_pay() {
		Optional<DTO> optionalDTO = DataTestUtil.getOptionalDTO(service, date, customer, totalInCents);
		String id = getIdFromOptionalDTO(optionalDTO);
		boolean wasPay = service.pay(optionalDTO, paymentDate);
		BankslipDTO dtoPersistedAndPaid = getBankslipDTO(id);

		assertTrue(wasPay);
		assertNotEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PENDING);
		assertEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PAID);
	}

	@Test
	public void test_cancel_a_bankslip() {
		Optional<DTO> optionalDTO = DataTestUtil.getOptionalDTO(service, date, customer, totalInCents);
		String id = getIdFromOptionalDTO(optionalDTO);
		boolean wasCancel = service.cancel(id);
		BankslipDTO dtoPersistedAndPaid = getBankslipDTO(id);

		assertTrue(wasCancel);
		assertNotEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.PENDING);
		assertEquals(dtoPersistedAndPaid.getStatus(), StatusEnum.CANCELED);
	}
	
	@Test
	public void test_Cancel_Not_Found() {
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
