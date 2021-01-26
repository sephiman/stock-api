package org.juanjo.stock.dto;

import org.juanjo.stock.utils.StockConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateStockDTO {
	@NotNull(message = "Name is mandatory")
	@Size(max = StockConstants.NAME_MAX_LENGTH, message = "Max size for name is 250 characters")
	private String name;
	@NotNull(message = "Current Price is mandatory")
	@Min(value = 0, message = "Current Price cannot be negative")
	private Double currentPrice;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}
}
