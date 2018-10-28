package com.bankslips.api.tests.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.IntStream;

import org.hamcrest.text.IsEmptyString;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bankslips.api.controller.BankslipsController;
import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.entity.BankslipEntity;
import com.bankslips.api.enums.StatusEnum;
import com.bankslips.api.repository.BankslipstRepository;
import com.bankslips.api.tests.util.DataTestUtil;
import com.bankslips.api.tests.util.DatabaseMockUtility;
import com.bankslips.api.tests.util.DatesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(value ="com.bankslips.api.*")
@ActiveProfiles("test")
public class BankslipsControllerTest {

	private JacksonTester<BankslipDTO> jacksonTester;
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@Autowired
	private BankslipsController controller;

	@Autowired
	BankslipstRepository repository;
	
	private int size = 10;
	private int daysAgo = -11;
	private String customer = "BankslipsControllerTest";
	private Date date = new Date();
	private BigDecimal totalInCents = BigDecimal.valueOf(1000.97);
	private String API_URL = "/rest/bankslips";
	private MediaType jsonUtf8 = MediaType.APPLICATION_JSON_UTF8;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
	            .build();
		
		objectMapper = new ObjectMapper();
		JacksonTester.initFields(this, objectMapper);

		IntStream.rangeClosed(daysAgo, size).forEach(i -> {
			this.repository.save(DatabaseMockUtility.newBankslip(customer + i, i));
		});
	}
	
	@After
	public final void tearDown() {
		this.repository.deleteAll();
	}
	
	@Test
	public void test_return_json_type_and_find_by_id() throws Exception {
		String id = DataTestUtil.getIdFindCustomer(repository, 1, customer);
		
		performRequestNoContentAndResulstWithoutFineAndPaymentDate(MockMvcRequestBuilders.get(API_URL + "/" + id), status().isOk());	
	}
	
	
	@Test
	public void test_not_found_by_id() throws Exception {
		String id = UUID.randomUUID().toString();

		performRequestNoContent(MockMvcRequestBuilders.get(API_URL + "/" + id), status().isNotFound());
	}
		
	@Test
	public void test_create_bankslip_return_is_correct() throws Exception {
		BankslipDTO bankslipDTO = (BankslipDTO) DatabaseMockUtility.getOneDTO(date, customer + Math.random(), totalInCents);
		bankslipDTO.setId(null);
		String jsonObject = jacksonTester.write(bankslipDTO).getJson();

		performRequestWithContentAndResultsWithoutFineAndPaymentDate(MockMvcRequestBuilders.post(API_URL), jsonObject);				
	}
	
	@Test
	public void test_bankslip_it_was_created_and_persisted() throws Exception {
		BankslipDTO bankslipDTO = (BankslipDTO) DatabaseMockUtility.getOneDTO(date, customer + Math.random(), totalInCents);
		bankslipDTO.setId(null);
		String json = jacksonTester.write(bankslipDTO).getJson();

		String id = new JSONObject(
				performRequestWithContentAndResultsWithoutFineAndPaymentDate(MockMvcRequestBuilders.post(API_URL), json)
					.andReturn()
					.getResponse().getContentAsString())
					.get("id").toString();
		
		String idPersisted = DataTestUtil.getBankslipEntityById(repository, id).get().getId();
		
		performRequestNoContentAndResulstWithoutFineAndPaymentDate(MockMvcRequestBuilders.get(API_URL + "/" + id), status().isOk());
		assertEquals(id, idPersisted);		
	}
	
	@Test
	public void test_not_create_bankslip_without_customer() throws Exception {
		BankslipDTO bankslipDTO  = (BankslipDTO) DatabaseMockUtility.getOneDTO(date, customer + Math.random(), totalInCents);
		bankslipDTO.setId(null);
		bankslipDTO.setCustomer(null);
		String json = jacksonTester.write(bankslipDTO).getJson();

		performRequestWithContent(MockMvcRequestBuilders.post(API_URL), status().isUnprocessableEntity(), json);				
	}
	
	@Test
	public void test_not_create_bankslip_without_due_date() throws Exception {
		BankslipDTO bankslipDTO = (BankslipDTO) DatabaseMockUtility.getOneDTO(date, customer + Math.random(), totalInCents);
		bankslipDTO.setId(null);
		bankslipDTO.setDueDate(null);
		String json = jacksonTester.write(bankslipDTO).getJson();

		performRequestWithContent(MockMvcRequestBuilders.post(API_URL), status().isUnprocessableEntity(), json);				
	}
	
	@Test
	public void test_not_create_bankslip_without_total_in_cents() throws Exception {
		BankslipDTO bankslipDTO = (BankslipDTO) DatabaseMockUtility.getOneDTO(date, customer + Math.random(), totalInCents);
		bankslipDTO.setId(null);
		bankslipDTO.setTotalInCents(null);
		String json = jacksonTester.write(bankslipDTO).getJson();

		performRequestWithContent(MockMvcRequestBuilders.post(API_URL), status().isUnprocessableEntity(), json);				
	}
	
	@Test
	public void test_not_create_bankslip_body_is_empty() throws Exception {
		performRequestWithContent(MockMvcRequestBuilders.post(API_URL), status().isUnprocessableEntity(), "{}");				
	}
	
	@Test
	public void test_not_create_bankslip_without_body() throws Exception {
		performRequestWithContent(MockMvcRequestBuilders.post(API_URL), status().isBadRequest(), "");				
	}

	@Test
	public void test_a_bankslip_due_date_greater_10_with_fine() throws Exception {
		String id = DataTestUtil.getIdFindCustomer(repository, -11, customer);
		
		performRequestNoContentAndResulstWithFine(MockMvcRequestBuilders.get(API_URL + "/" + id), status().isOk());	
	}
	
	@Test
	public void test_a_bankslip_due_date_greater_10_days_and_fine_is_correct() throws Exception {
		String id = DataTestUtil.getIdFindCustomer(repository, -11, customer);
		BankslipEntity entity = this.repository.findById(id).get();			
		BigDecimal testFine = getTestFine(entity.getDueDate(), entity.getTotalInCents());		
		String fine = getStringFineFromResults(id);		
		BigDecimal fineFromResults = new BigDecimal(fine);
		
		assertEquals(testFine, fineFromResults);
	}

	
	@Test
	public void test_cancel_a_bankslip() throws Exception {
		String id = DataTestUtil.getIdFindCustomer(repository, 2, customer);		
		
		performRequestNoContent(MockMvcRequestBuilders.delete(API_URL + "/" + id), status().isNoContent());
		
		BankslipEntity entity = this.repository.findById(id).get();
		
		assertEquals(StatusEnum.CANCELED, entity.getStatus());
	}
	
	@Test
	public void test_not_found_bankslip_to_cancel() throws Exception {
		String id = UUID.randomUUID().toString();
		performRequestNoContent(MockMvcRequestBuilders.delete(API_URL + "/" + id), status().isNotFound());
	}
	
	@Test
	public void test_find_all_bankslips() throws Exception {
		int countBankslips = (int) this.repository.findAll().spliterator().getExactSizeIfKnown();
		performRequestNoContentAndResulstIsArrayAndSize(MockMvcRequestBuilders.get(API_URL), status().isOk(), countBankslips);
	}
	
	@Test
	public void test_find_all_not_found_bankslips() throws Exception {
		this.repository.deleteAll();
		performRequestNoContentAndNotFoundResulst(MockMvcRequestBuilders.get(API_URL), status().isNoContent());
		
	}
	
	@Test
	public void payment_bankslip_sucess() throws Exception {
		String id = DataTestUtil.getIdFindCustomer(repository, 1, customer);
		BankslipDTO bankslipDTO = new BankslipDTO();
		bankslipDTO.setId(null);
		bankslipDTO.setPaymentDate(date);
		String json = jacksonTester.write(bankslipDTO).getJson();
		
		performRequestWithContent(MockMvcRequestBuilders.post(API_URL + "/" + id + "/payments"), status().isNoContent(), json);
		
		BankslipEntity entity = this.repository.findById(id).get();
		
		assertEquals(entity.getStatus(), StatusEnum.PAID);
		assertEquals(DatesUtil.simpleDate(bankslipDTO.getPaymentDate()), DatesUtil.simpleDate(entity.getPaymentDate()));
		
	}
	
	@Test
	public void payment_bankslip_not_found_id() throws Exception {
		String id = UUID.randomUUID().toString();
		JSONObject json = new JSONObject();
		json.put("payment_date", DatesUtil.simpleDate(date).toString());
		
		performRequestWithContent(MockMvcRequestBuilders.post(API_URL + "/" + id + "/payments"), status().isNotFound(), "{}");
	}
	
	
	@Test
	public void test_response_handler_message_not_readable() throws Exception {
		JSONObject json = new JSONObject();
		json.put("customer", customer);
		json.put("due_date", DatesUtil.simpleDate(date));
		json.put("total_in_cents","InvalidData");
		
		performRequestWithContent(MockMvcRequestBuilders.post(API_URL), status().isBadRequest(), json.toString());
	}
	
	private ResultActions performRequestWithContentAndResultsWithoutFineAndPaymentDate(MockHttpServletRequestBuilder request,
			String json) throws Exception {
				
		ResultActions resultsAction = this.mockMvc
				.perform(request
				.content(json)
				.contentType(jsonUtf8)
				.accept(jsonUtf8))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.due_date").exists())
				.andExpect(jsonPath("$.customer").exists())
				.andExpect(jsonPath("$.total_in_cents").exists())
				.andExpect(jsonPath("$.status").exists())
				.andExpect(jsonPath("$.fine").doesNotExist())
				.andExpect(jsonPath("$.payment_date").doesNotExist());
		
		return resultsAction;
	}
	
	private ResultActions performRequestNoContentAndResulstWithoutFineAndPaymentDate(MockHttpServletRequestBuilder request,
			ResultMatcher status) throws Exception {
		
		ResultActions resultsAction = this.mockMvc
				.perform(request
				.contentType(jsonUtf8)
				.accept(jsonUtf8))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status)
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.due_date").exists())
				.andExpect(jsonPath("$.customer").exists())
				.andExpect(jsonPath("$.total_in_cents").exists())
				.andExpect(jsonPath("$.status").exists())
				.andExpect(jsonPath("$.fine").doesNotExist())
				.andExpect(jsonPath("$.payment_date").doesNotExist());
		
		return resultsAction;
	}
	
	private ResultActions performRequestNoContentAndResulstWithFine(MockHttpServletRequestBuilder request,
			ResultMatcher status) throws Exception {
		
		ResultActions resultsAction = this.mockMvc
				.perform(request
				.contentType(jsonUtf8)
				.accept(jsonUtf8))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status)
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.due_date").exists())
				.andExpect(jsonPath("$.customer").exists())
				.andExpect(jsonPath("$.total_in_cents").exists())
				.andExpect(jsonPath("$.status").exists())
				.andExpect(jsonPath("$.fine").exists())
				.andExpect(jsonPath("$.payment_date").doesNotExist());
		
		return resultsAction;
	}
	
	private ResultActions performRequestNoContent(MockHttpServletRequestBuilder request, ResultMatcher status)
			throws Exception {

		ResultActions resultsAction = this.mockMvc
				.perform(request.contentType(jsonUtf8)
				.accept(jsonUtf8))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status);

		return resultsAction;
	}
	
	private ResultActions performRequestNoContentAndResulstIsArrayAndSize(MockHttpServletRequestBuilder request, ResultMatcher status, int size)
			throws Exception {

		ResultActions resultsAction = this.mockMvc
				.perform(request.contentType(jsonUtf8)
				.accept(jsonUtf8))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status)
				.andExpect(jsonPath("$", hasSize(size)))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty());		

		return resultsAction;
	}
	
	private ResultActions performRequestNoContentAndNotFoundResulst(MockHttpServletRequestBuilder request, ResultMatcher status)
			throws Exception {

		ResultActions resultsAction = this.mockMvc
				.perform(request.contentType(jsonUtf8)
				.accept(jsonUtf8))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status)
				.andExpect(content().string(IsEmptyString.isEmptyOrNullString()));

		return resultsAction;
	}
	
	private ResultActions performRequestWithContent(MockHttpServletRequestBuilder request, ResultMatcher status, String json)
			throws Exception {

		ResultActions resultsAction = this.mockMvc
				.perform(request.contentType(jsonUtf8)
				.content(json)
				.accept(jsonUtf8))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status);

		return resultsAction;
	}
	
	private BigDecimal getTestFine(Date dueDate, BigDecimal totalInCents) {
		BigDecimal testFine = null;
		Date today = new Date();
		long days = ChronoUnit.DAYS.between(dueDate.toInstant(), today.toInstant());
		
		if (days > 10) {
			testFine = (totalInCents.multiply(BigDecimal.valueOf(0.01))).multiply(BigDecimal.valueOf(days));
		}
		if (days > 0 && days <= 10) {
			testFine = (totalInCents.multiply(BigDecimal.valueOf(0.005))).multiply(BigDecimal.valueOf(days));
		}
		
		return testFine;
	}
	
	private String getStringFineFromResults(String id) throws JSONException, UnsupportedEncodingException, Exception {
		return new JSONObject(
				performRequestNoContentAndResulstWithFine(MockMvcRequestBuilders.get(API_URL + "/" + id),
					status().isOk())
					.andReturn()
					.getResponse()
					.getContentAsString())
					.get("fine")
					.toString();
	}

}
