package org.juanjo.stock.persistence;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.stock.dao.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
public class StockRepositoryTest {
	@Autowired
	private StockRepository repository;

	@Test
	void testSave() {
		Stock stock = new Stock();
		stock.setName(RandomStringUtils.randomAlphanumeric(32));
		stock.setCurrentPrice(RandomUtils.nextDouble());
		Stock inserted = repository.save(stock);
		assertNotNull(inserted.getId());
		assertNotNull(inserted.getLastUpdate());
		assertEquals(stock.getName(), inserted.getName());
		assertEquals(stock.getCurrentPrice(), inserted.getCurrentPrice());
	}

	@Test
	void testSaveAndUpdate() {
		String initialName = RandomStringUtils.randomAlphanumeric(32);
		Double initialPrice = RandomUtils.nextDouble();
		Stock stock = new Stock();
		stock.setName(initialName);
		stock.setCurrentPrice(initialPrice);
		Stock inserted = repository.save(stock);
		assertEquals(initialName, inserted.getName());
		assertEquals(initialPrice, inserted.getCurrentPrice());
		String newerName = RandomStringUtils.randomAlphanumeric(32);
		Double newerPrice = RandomUtils.nextDouble();
		inserted.setName(newerName);
		inserted.setCurrentPrice(newerPrice);
		Stock updated = repository.save(inserted);
		assertEquals(inserted.getId(), updated.getId());
		assertEquals(newerName, updated.getName());
		assertEquals(newerPrice, updated.getCurrentPrice());
		assertNotEquals(initialName, updated.getName());
		assertNotEquals(initialPrice, updated.getCurrentPrice());
	}

	@Test
	void testFindById() {
		Stock stock = new Stock();
		stock.setName(RandomStringUtils.randomAlphanumeric(32));
		stock.setCurrentPrice(RandomUtils.nextDouble());
		Stock inserted = repository.save(stock);
		Optional<Stock> found = repository.findById(inserted.getId());
		if (found.isPresent()) {
			assertNotNull(found.get().getId());
			assertNotNull(found.get().getLastUpdate());
			assertEquals(stock.getName(), found.get().getName());
			assertEquals(stock.getCurrentPrice(), found.get().getCurrentPrice());
		} else {
			fail("Not found");
		}
	}

	@Test
	void testFindAll() {
		List<Stock> result = new ArrayList<>();
		repository.findAll().forEach(result::add);
		assertEquals(21, result.size());
		result.forEach(stock -> {
			assertNotNull(stock.getId());
			assertNotNull(stock.getName());
			assertNotNull(stock.getCurrentPrice());
			assertNotNull(stock.getLastUpdate());
		});
	}
}
