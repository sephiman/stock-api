package org.juanjo.stock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.stock.dto.CreateStockDTO;
import org.juanjo.stock.dto.StockDTO;
import org.juanjo.stock.dto.UpdateStockDTO;
import org.juanjo.stock.utils.StockConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockControllerIT {
	@Autowired
	private StockController controller;
	private ObjectMapper mapper;

	@BeforeAll
	public void setup() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
	}

	@Test
	public void testCreateStockOK() throws JsonProcessingException {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		String jsonResponse =
				RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE)
						.when().post("/api/stocks").then().statusCode(HttpStatus.CREATED.value()).contentType(ContentType.JSON).extract()
						.asString();
		StockDTO response = mapper.readValue(jsonResponse, StockDTO.class);
		assertNotNull(response);
		assertNotNull(response.getId());
		assertEquals(initialRequest.getName(), response.getName());
		assertEquals(initialRequest.getCurrentPrice(), response.getCurrentPrice());
		assertNotNull(response.getLastUpdate());
	}

	@Test
	public void testCreateStockKONoName() {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.post("/api/stocks").then().statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void testCreateStockKONameMaxSize() {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(StockConstants.NAME_MAX_LENGTH + 1));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.post("/api/stocks").then().statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void testCreateStockKOPriceNegative() {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble() * -1);
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.post("/api/stocks").then().statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void testGetStockByIdOk() throws JsonProcessingException {
		StockDTO stock = createStock();
		String jsonResponse =
				RestAssuredMockMvc.given().standaloneSetup(controller).when().get("/api/stocks/{stockId}", stock.getId()).then()
						.statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON).extract().asString();
		StockDTO response = mapper.readValue(jsonResponse, StockDTO.class);
		assertNotNull(response);
		assertEquals(stock.getId(), response.getId().longValue());
		assertEquals(stock.getName(), response.getName());
		assertEquals(stock.getCurrentPrice(), response.getCurrentPrice());
		assertEquals(stock.getLastUpdate(), response.getLastUpdate());
	}

	@Test
	public void testGetStockByIdNotFound() {
		long stockId = -1;
		RestAssuredMockMvc.given().standaloneSetup(controller).when().get("/api/stocks/{stockId}", stockId).then()
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void testListStocks() throws JsonProcessingException {
		StockDTO createdStock = createStock();
		String jsonResponse =
				RestAssuredMockMvc.given().standaloneSetup(controller).when().get("/api/stocks").then().statusCode(HttpStatus.OK.value())
						.contentType(ContentType.JSON).extract().asString();
		List<StockDTO> stockList = mapper.readValue(jsonResponse, new TypeReference<List<StockDTO>>() {
		});
		assertNotNull(stockList);
		assertFalse(stockList.isEmpty());
		StockDTO foundStock = stockList.stream().filter(s -> s.getId().equals(createdStock.getId())).findFirst().orElse(null);
		assertNotNull(foundStock);
		assertEquals(createdStock.getId(), foundStock.getId());
		assertEquals(createdStock.getName(), foundStock.getName());
		assertEquals(createdStock.getCurrentPrice(), foundStock.getCurrentPrice());
		assertEquals(createdStock.getLastUpdate(), foundStock.getLastUpdate());
	}

	@Test
	public void testUpdateStockOK() throws JsonProcessingException {
		StockDTO createdStock = createStock();
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", createdStock.getId()).then().statusCode(HttpStatus.NO_CONTENT.value());
		StockDTO updatedStock = getStockById(createdStock.getId());
		assertNotNull(updatedStock);
		assertEquals(initialRequest.getName(), updatedStock.getName());
		assertEquals(initialRequest.getCurrentPrice(), updatedStock.getCurrentPrice());
		assertNotEquals(createdStock.getName(), updatedStock.getName());
		assertNotEquals(createdStock.getCurrentPrice(), updatedStock.getCurrentPrice());
	}

	@Test
	public void testUpdateStockOKLastUpdateChanged() throws JsonProcessingException, InterruptedException {
		StockDTO createdStock = createStock();
		Thread.sleep(1000);
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", createdStock.getId()).then().statusCode(HttpStatus.NO_CONTENT.value());
		StockDTO updatedStock = getStockById(createdStock.getId());
		assertNotNull(updatedStock);
		assertEquals(initialRequest.getName(), updatedStock.getName());
		assertEquals(initialRequest.getCurrentPrice(), updatedStock.getCurrentPrice());
		assertTrue(updatedStock.getLastUpdate().isAfter(createdStock.getLastUpdate()));
		assertNotEquals(createdStock.getName(), updatedStock.getName());
		assertNotEquals(createdStock.getCurrentPrice(), updatedStock.getCurrentPrice());
	}

	@Test
	public void testUpdateStockKONotFound() {
		long stockId = -1;
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", stockId).then().statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void testUpdateStockKONoName() {
		long stockId = RandomUtils.nextLong();
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", stockId).then().statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void testUpdateStockKONameMaxSize() {
		long stockId = RandomUtils.nextLong();
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(StockConstants.NAME_MAX_LENGTH + 1));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", stockId).then().statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void testUpdateStockKOPriceNegative() {
		long stockId = RandomUtils.nextLong();
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble() * -1);
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", stockId).then().statusCode(HttpStatus.BAD_REQUEST.value());
	}

	/**
	 * Generates a new stock for verification purpose. Verified in test testCreateStockOK
	 *
	 * @return created stock
	 * @throws JsonProcessingException if parsing fails
	 */
	private StockDTO createStock() throws JsonProcessingException {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		String jsonResponse =
				RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE)
						.when().post("/api/stocks").then().statusCode(HttpStatus.CREATED.value()).contentType(ContentType.JSON).extract()
						.asString();
		return mapper.readValue(jsonResponse, StockDTO.class);
	}

	/**
	 * Gets the stock given an identifier. Verified in method testGetStockByIdOk
	 *
	 * @param stockId to find
	 * @return stockDTO
	 * @throws JsonProcessingException if parsing fails
	 */
	private StockDTO getStockById(Long stockId) throws JsonProcessingException {
		String jsonResponse = RestAssuredMockMvc.given().standaloneSetup(controller).when().get("/api/stocks/{stockId}", stockId).then()
				.statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON).extract().asString();
		return mapper.readValue(jsonResponse, StockDTO.class);
	}
}
