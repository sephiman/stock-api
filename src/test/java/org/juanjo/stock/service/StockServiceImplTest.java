package org.juanjo.stock.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.stock.dao.Stock;
import org.juanjo.stock.dto.CreateStockDTO;
import org.juanjo.stock.dto.StockDTO;
import org.juanjo.stock.dto.UpdateStockDTO;
import org.juanjo.stock.exception.NotFoundException;
import org.juanjo.stock.persistence.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockServiceImplTest {
	@InjectMocks
	private StockServiceImpl service;
	@Mock
	private StockRepository stockRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetByIdOK() {
		long stockId = RandomUtils.nextLong();
		Stock stockFromDb = generateRandomStock();
		when(stockRepository.findById(stockId)).thenReturn(Optional.of(stockFromDb));

		StockDTO result = service.getById(stockId);
		assertNotNull(result);
		assertEquals(stockFromDb.getId(), result.getId());
		assertEquals(stockFromDb.getName(), result.getName());
		assertEquals(stockFromDb.getCurrentPrice(), result.getCurrentPrice());
		assertEquals(stockFromDb.getLastUpdate(), result.getLastUpdate());
	}

	@Test
	public void testGetByIdNotFound() {
		long stockId = RandomUtils.nextLong();
		when(stockRepository.findById(stockId)).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> service.getById(stockId));
	}

	@Test
	public void testListStocksOKMany() {
		int nItems = RandomUtils.nextInt(1, 10);
		List<Stock> dbStocks = new ArrayList<>();
		IntStream.range(0, nItems).forEach(i -> dbStocks.add(generateRandomStock()));
		when(stockRepository.findAll()).thenReturn(dbStocks);
		List<StockDTO> result = service.listStocks();
		assertNotNull(result);
		assertEquals(nItems, result.size());
		IntStream.range(0, nItems).forEach(i -> {
			assertEquals(dbStocks.get(i).getId(), result.get(i).getId());
			assertEquals(dbStocks.get(i).getName(), result.get(i).getName());
			assertEquals(dbStocks.get(i).getCurrentPrice(), result.get(i).getCurrentPrice());
			assertEquals(dbStocks.get(i).getLastUpdate(), result.get(i).getLastUpdate());
		});
	}

	@Test
	public void testListStocksOKEmpty() {
		when(stockRepository.findAll()).thenReturn(Collections.emptyList());
		List<StockDTO> result = service.listStocks();
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testCreateStockOk() {
		Stock createdStock = generateRandomStock();
		ArgumentCaptor<Stock> saveCaptor = ArgumentCaptor.forClass(Stock.class);
		doReturn(createdStock).when(stockRepository).save(saveCaptor.capture());
		CreateStockDTO request = new CreateStockDTO();
		request.setName(RandomStringUtils.randomAlphanumeric(12));
		request.setCurrentPrice(RandomUtils.nextDouble());
		StockDTO result = service.createStock(request);
		assertNotNull(result);
		assertEquals(createdStock.getId(), result.getId());
		assertEquals(createdStock.getName(), result.getName());
		assertEquals(createdStock.getCurrentPrice(), result.getCurrentPrice());
		assertEquals(createdStock.getLastUpdate(), result.getLastUpdate());
		Stock stockToDB = saveCaptor.getValue();
		assertNotNull(stockToDB);
		assertEquals(request.getName(), stockToDB.getName());
		assertEquals(request.getCurrentPrice(), stockToDB.getCurrentPrice());
	}

	@Test
	public void testUpdateStockNotFound() {
		long stockId = RandomUtils.nextLong();
		when(stockRepository.findById(stockId)).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> service.updateStock(stockId, new UpdateStockDTO()));
	}

	@Test
	public void testUpdateStockOk() {
		long stockId = RandomUtils.nextLong();
		Stock existingStock = generateRandomStock();
		when(stockRepository.findById(stockId)).thenReturn(Optional.of(existingStock));
		UpdateStockDTO request = new UpdateStockDTO();
		request.setName(RandomStringUtils.randomAlphanumeric(12));
		request.setCurrentPrice(RandomUtils.nextDouble());
		service.updateStock(stockId, request);
		ArgumentCaptor<Stock> saveCaptor = ArgumentCaptor.forClass(Stock.class);
		verify(stockRepository).save(saveCaptor.capture());
		Stock stockToDB = saveCaptor.getValue();
		assertNotNull(stockToDB);
		assertEquals(request.getName(), stockToDB.getName());
		assertEquals(request.getCurrentPrice(), stockToDB.getCurrentPrice());
	}

	private Stock generateRandomStock() {
		Stock stockFromDb = new Stock();
		stockFromDb.setId(RandomUtils.nextLong());
		stockFromDb.setName(RandomStringUtils.randomAlphanumeric(32));
		stockFromDb.setCurrentPrice(RandomUtils.nextDouble());
		stockFromDb.setLastUpdate(new Date());
		return stockFromDb;
	}
}
