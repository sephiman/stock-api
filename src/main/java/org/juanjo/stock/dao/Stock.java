package org.juanjo.stock.dao;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "stock")
public class Stock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Double currentPrice;
	private LocalDateTime lastUpdate;

	@PreUpdate
	@PrePersist
	protected void onModification() {
		lastUpdate = LocalDateTime.now();
	}
}
