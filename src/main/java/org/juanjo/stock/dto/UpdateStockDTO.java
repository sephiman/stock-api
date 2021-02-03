package org.juanjo.stock.dto;

import lombok.Data;
import org.juanjo.stock.utils.StockConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateStockDTO {
	@NotNull(message = "Name is mandatory")
	@Size(min = StockConstants.NAME_MIN_LENGTH, max = StockConstants.NAME_MAX_LENGTH, message = "Name length must be between 1 and 250 characters")
	private String name;
	@NotNull(message = "Current Price is mandatory")
	@Min(value = 0, message = "Current Price cannot be negative")
	private Double currentPrice;
}
