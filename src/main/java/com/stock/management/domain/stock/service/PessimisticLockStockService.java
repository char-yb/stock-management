package com.stock.management.domain.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stock.management.domain.stock.domain.Stock;
import com.stock.management.domain.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PessimisticLockStockService {
	private final StockRepository stockRepository;

	/**
	 * 장점: 충돌이 빈번하게 일어난다면 Optimistic Lock보다 좋을 수 있다.
	 * 락을 업데이트를 통해 제어하기에 정합성이 보장된다.
	 * 단점: 별도의 락을 잡기에 성능이 감소될 수도 있다.
	 * @param id
	 * @param quantity
	 */
	@Transactional
	public void decreaseQuantity(Long id, Long quantity) {
		Stock stock = stockRepository.findByIdWithAndPessimisticLock(id);
		stock.decrease(quantity);
		stockRepository.save(stock);
	}
}
