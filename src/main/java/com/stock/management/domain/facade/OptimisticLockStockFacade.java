package com.stock.management.domain.facade;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stock.management.domain.stock.service.OptimisticStockService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

	private final OptimisticStockService optimisticStockService;

	public void decrease(Long id, Long quantity) throws InterruptedException {

		// 업데이트를 실패했을 때 재시도를 해야함.
		while (true) {
			try {
				optimisticStockService.decreaseQuantity(id, quantity);
				// 성공 시 break
				break;
			} catch (Exception e) {
				// 실패했을 시, sleep을 주어 재시도
				Thread.sleep(100);
			}
		}
	}
}
