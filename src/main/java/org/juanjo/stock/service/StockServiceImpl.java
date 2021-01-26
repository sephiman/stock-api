package org.juanjo.stock.service;

import org.juanjo.stock.dao.Stock;
import org.juanjo.stock.dto.CreateStockDTO;
import org.juanjo.stock.dto.StockDTO;
import org.juanjo.stock.dto.UpdateStockDTO;
import org.juanjo.stock.exception.NotFoundException;
import org.juanjo.stock.persistence.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StockServiceImpl implements StockService {
	@Autowired
	private StockRepository stockRepository;

	@Override
	public StockDTO getById(Long stockId) {
		Stock stock = stockRepository.findById(stockId).orElseThrow(NotFoundException::new);
		return new StockDTO(stock);
	}

	@Override
	public List<StockDTO> listStocks() {
		return StreamSupport.stream(stockRepository.findAll().spliterator(), false).map(StockDTO::new).collect(Collectors.toList());
	}

	@Override
	public StockDTO createStock(CreateStockDTO request) {
		Stock stock = new Stock();
		stock.setName(request.getName());
		stock.setCurrentPrice(request.getCurrentPrice());
		return new StockDTO(stockRepository.save(stock));
	}

	@Override
	public void updateStock(Long stockId, UpdateStockDTO request) {
		Stock stock = stockRepository.findById(stockId).orElseThrow(NotFoundException::new);
		stock.setName(request.getName());
		stock.setCurrentPrice(request.getCurrentPrice());
		stockRepository.save(stock);
	}
}
