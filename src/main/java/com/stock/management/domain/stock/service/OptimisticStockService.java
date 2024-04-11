package com.stock.management.domain.stock.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.management.domain.stock.domain.Stock;
import com.stock.management.domain.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptimisticStockService {

	private final StockRepository stockRepository;

	/**
	 * OptimisticLock은 실패를 했을때, 재시도를 해야 한다.
	 * @param id
	 * @param quantity
	 */
	@Transactional
	public void decreaseQuantity(Long id, Long quantity) {
		Stock stock = stockRepository.findByIdWithOptimisticLock(id);
		stock.decrease(quantity);
		stockRepository.save(stock);
	}

}
