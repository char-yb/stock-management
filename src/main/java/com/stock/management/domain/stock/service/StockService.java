package com.stock.management.domain.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.management.domain.stock.domain.Stock;
import com.stock.management.domain.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// @Transactional
public class StockService {

	private final StockRepository stockRepository;

	// Transactional 어노테이션의 동작방식으로, synchronized 사용 시 만든 클래스를 래핑한 클래스로 새로 생성하여 실행하게 된다.

	/**
	 * Java의 synchronized는 하나의 프로세스안에서만 보장이 된다.
	 * synchronized 문제점으로는 만약, 서버가 여러 대 존재하는 경우에서 발생된다.
	 * 한대의 서버가 데이터 접근을 위한 프로세스를 실행중, 다른 서버가 데이터 접근을 할 경우 문제가 발생된다. (Race Condition)
	 * @param stockId
	 * @param quantity
	 */
	/**
	 * named lock 시에는 부모의 트랜잭션과 별도로 실행되어야 하기에 Propagation 을 변경해줘야한다.
	 * @param stockId
	 * @param quantity
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void decreaseQuantity(Long stockId, Long quantity) {
		/** 재고 감소 요구사항
		 * Stock 조회
		 * 재고를 감소한 뒤
		 * 갱신된 값 저장
		 */

		Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new RuntimeException("존재하지 않는 재고입니다."));
		stock.decrease(quantity);
		stockRepository.saveAndFlush(stock);
	}
}
