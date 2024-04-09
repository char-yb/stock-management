package com.stock.management.domain.stock.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.stock.management.domain.stock.domain.Stock;
import com.stock.management.domain.stock.repository.StockRepository;

@SpringBootTest
@ActiveProfiles("test")
class StockServiceTest {

	@Autowired
	private StockService stockService;
	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	public void setUp() {
		Stock stock = Stock.createStock(1L, 100L);
		stockRepository.saveAndFlush(stock);
	}

	@AfterEach
	public void afterCase() {
		stockRepository.deleteAll();
	}

	@Test
	public void 재고를_감소시킨다() {
		stockService.decreaseQuantity(1L, 1L);

		Stock stock = stockRepository.findById(1L).orElseThrow(() -> new RuntimeException("존재하지 않는 재고입니다."));
		assertEquals(99L, stock.getQuantity());
	}
}