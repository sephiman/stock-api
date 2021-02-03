package org.juanjo.stock.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.juanjo.stock.dao.Stock;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StockDTO {
	private Long id;
	private String name;
	private Double currentPrice;
	private LocalDateTime lastUpdate;

	public StockDTO(Stock stock) {
		this.id = stock.getId();
		this.name = stock.getName();
		this.currentPrice = stock.getCurrentPrice();
		this.lastUpdate = stock.getLastUpdate();
	}
}
