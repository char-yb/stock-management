package com.stock.management.domain.facade;

import org.springframework.stereotype.Component;

import com.stock.management.domain.stock.repository.RedisLockRepository;
import com.stock.management.domain.stock.service.StockService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

	private final RedisLockRepository redisLockRepository;
	private final StockService stockService;

	public void decrease(Long key, Long quantity) throws InterruptedException {
		while (!redisLockRepository.lock(key)) {
			// lock 획득을 재시도한다. 이렇게 해야 redis에 갈 수 있는 부하를 줄인다.
			Thread.sleep(100);
		}

		try {
			stockService.decreaseQuantity(key, quantity);
		} finally {
			redisLockRepository.unlock(key);
		}
	}
}
