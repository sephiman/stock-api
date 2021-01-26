package org.juanjo.stock.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.stock.dto.CreateStockDTO;
import org.juanjo.stock.dto.UpdateStockDTO;
import org.juanjo.stock.service.StockService;
import org.juanjo.stock.utils.StockConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


public class StockControllerTest {
	@InjectMocks
	private StockController controller;
	@Mock
	private StockService stockService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetStockById() {
		long stockId = RandomUtils.nextLong();
		RestAssuredMockMvc.given().standaloneSetup(controller).when().get("/api/stocks/{stockId}", stockId).then()
				.statusCode(HttpStatus.OK.value());
		verify(stockService).getById(stockId);
	}

	@Test
	public void testListStocks() {
		RestAssuredMockMvc.given().standaloneSetup(controller).when().get("/api/stocks").then().statusCode(HttpStatus.OK.value());
		verify(stockService).listStocks();
	}

	@Test
	public void testCreateStockOK() {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.post("/api/stocks").then().statusCode(HttpStatus.CREATED.value());
		ArgumentCaptor<CreateStockDTO> requestCaptor = ArgumentCaptor.forClass(CreateStockDTO.class);
		verify(stockService).createStock(requestCaptor.capture());
		CreateStockDTO requestToService = requestCaptor.getValue();
		assertNotNull(requestToService);
		assertEquals(initialRequest.getName(), requestToService.getName());
		assertEquals(initialRequest.getCurrentPrice(), requestToService.getCurrentPrice());
	}

	@Test
	public void testCreateStockKONoName() {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.post("/api/stocks").then().statusCode(HttpStatus.BAD_REQUEST.value());
		verify(stockService, never()).createStock(any());
	}

	@Test
	public void testCreateStockKONameMaxSize() {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(StockConstants.NAME_MAX_LENGTH + 1));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.post("/api/stocks").then().statusCode(HttpStatus.BAD_REQUEST.value());
		verify(stockService, never()).createStock(any());
	}

	@Test
	public void testCreateStockKOPriceNegative() {
		CreateStockDTO initialRequest = new CreateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble() * -1);
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.post("/api/stocks").then().statusCode(HttpStatus.BAD_REQUEST.value());
		verify(stockService, never()).createStock(any());
	}

	@Test
	public void testUpdateStockOK() {
		long stockId = RandomUtils.nextLong();
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", stockId).then().statusCode(HttpStatus.NO_CONTENT.value());
		ArgumentCaptor<UpdateStockDTO> requestCaptor = ArgumentCaptor.forClass(UpdateStockDTO.class);
		verify(stockService).updateStock(eq(stockId), requestCaptor.capture());
		UpdateStockDTO requestToService = requestCaptor.getValue();
		assertNotNull(requestToService);
		assertEquals(initialRequest.getName(), requestToService.getName());
		assertEquals(initialRequest.getCurrentPrice(), requestToService.getCurrentPrice());
	}

	@Test
	public void testUpdateStockKONoName() {
		long stockId = RandomUtils.nextLong();
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", stockId).then().statusCode(HttpStatus.BAD_REQUEST.value());
		verify(stockService, never()).updateStock(anyLong(), any());
	}

	@Test
	public void testUpdateStockKONameMaxSize() {
		long stockId = RandomUtils.nextLong();
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(StockConstants.NAME_MAX_LENGTH + 1));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble());
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", stockId).then().statusCode(HttpStatus.BAD_REQUEST.value());
		verify(stockService, never()).updateStock(anyLong(), any());
	}

	@Test
	public void testUpdateStockKOPriceNegative() {
		long stockId = RandomUtils.nextLong();
		UpdateStockDTO initialRequest = new UpdateStockDTO();
		initialRequest.setName(RandomStringUtils.randomAlphanumeric(32));
		initialRequest.setCurrentPrice(RandomUtils.nextDouble() * -1);
		RestAssuredMockMvc.given().standaloneSetup(controller).body(initialRequest).contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.put("/api/stocks/{stockId}", stockId).then().statusCode(HttpStatus.BAD_REQUEST.value());
		verify(stockService, never()).updateStock(anyLong(), any());
	}
}
