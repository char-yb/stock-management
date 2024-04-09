package com.stock.management.domain.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stock.management.domain.stock.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
