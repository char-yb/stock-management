package com.stock.management.domain.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.management.domain.stock.repository.LockRepository;
import com.stock.management.domain.stock.service.StockService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

	private final LockRepository lockRepository;
	private final StockService stockService;

	// @Transactional
	public void decrease(Long id, Long quantity) {
		try {
			// System.out.println("id: " + id.toString());
			// 락 획득
			lockRepository.getLock(id.toString());
			// 재고 감소
			stockService.decreaseQuantity(id, quantity);
		} finally {
			lockRepository.releaseLock(id.toString());
		}
	}
}
