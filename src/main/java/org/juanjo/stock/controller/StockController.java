package org.juanjo.stock.controller;

import org.juanjo.stock.dto.CreateStockDTO;
import org.juanjo.stock.dto.StockDTO;
import org.juanjo.stock.dto.UpdateStockDTO;
import org.juanjo.stock.exception.NotFoundException;
import org.juanjo.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Controller to handle stocks
 */
@RestController
@RequestMapping(value = "/api/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockController {
	@Autowired
	private StockService stockService;

	/**
	 * Gets the stock by the {@code stockId} provided.
	 *
	 * @param stockId that identify the stock
	 * @return found stock
	 * @throws NotFoundException when the resource is not found
	 */
	@GetMapping("/{stockId}")
	public StockDTO getStockById(@PathVariable long stockId) throws NotFoundException {
		return stockService.getById(stockId);
	}

	/**
	 * Retrieves the full list of existing stocks
	 *
	 * @return list of stocks
	 */
	@GetMapping
	public List<StockDTO> listStocks() {
		return stockService.listStocks();
	}

	/**
	 * Creates the stock providing the necessary data
	 *
	 * @param request with the needed parameters to create the stock
	 * @return created stock
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public StockDTO createStock(@RequestBody @Valid CreateStockDTO request) {
		return stockService.createStock(request);
	}

	/**
	 * Updates the attributes of the given {@code stockId}
	 *
	 * @param stockId to update
	 * @param request with all stock attributes to update
	 * @throws NotFoundException when the resource is not found
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/{stockId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateStock(@PathVariable long stockId, @RequestBody @Valid UpdateStockDTO request) throws NotFoundException {
		stockService.updateStock(stockId, request);
	}
}
