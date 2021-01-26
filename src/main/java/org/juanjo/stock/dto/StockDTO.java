package org.juanjo.stock.dto;

import org.juanjo.stock.dao.Stock;

import java.util.Date;

public class StockDTO {
	private Long id;
	private String name;
	private Double currentPrice;
	private Date lastUpdate;

	public StockDTO() {
	}

	public StockDTO(Stock stock) {
		this.id = stock.getId();
		this.name = stock.getName();
		this.currentPrice = stock.getCurrentPrice();
		this.lastUpdate = stock.getLastUpdate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
