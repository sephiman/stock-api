package org.juanjo.stock.service;

import org.juanjo.stock.dto.CreateStockDTO;
import org.juanjo.stock.dto.StockDTO;
import org.juanjo.stock.dto.UpdateStockDTO;
import org.juanjo.stock.exception.NotFoundException;

import java.util.List;

/**
 * Service that manages the stock
 */
public interface StockService {

	/**
	 * Gets the stock by the {@code stockId} provided.
	 *
	 * @param stockId that identify the stock
	 * @return found stock
	 * @throws NotFoundException when stock is not found
	 */
	StockDTO getById(Long stockId) throws NotFoundException;

	/**
	 * Retrieves the full list of existing stocks
	 *
	 * @return list of stocks
	 */
	List<StockDTO> listStocks();

	/**
	 * Creates the stock providing the necessary data
	 *
	 * @param request with the needed parameters to create the stock
	 * @return created stock
	 */
	StockDTO createStock(CreateStockDTO request);

	/**
	 * Updates the attributes of the given {@code stockId}
	 *
	 * @param stockId to update
	 * @param request with all stock attributes to update
	 * @throws NotFoundException when stock is not found
	 */
	void updateStock(Long stockId, UpdateStockDTO request) throws NotFoundException;
}
