package org.juanjo.stock.persistence;

import org.juanjo.stock.dao.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Database repository for Stocks
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
