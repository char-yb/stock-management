package com.stock.management.domain.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stock.management.domain.stock.domain.Stock;
import com.stock.management.domain.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

	private final StockRepository stockRepository;

	public void decreaseQuantity(Long stockId, Long quantity) {
		/** 재고 감소 요구사항
		 * Stock 조회
		 * 재고를 감소한 뒤
		 * 갱신된 값 저장
		 */

		Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new RuntimeException("존재하지 않는 재고입니다."));
		stock.decrease(quantity);
	}
}
