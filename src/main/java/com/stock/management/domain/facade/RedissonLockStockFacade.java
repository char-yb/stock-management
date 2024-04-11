package com.stock.management.domain.facade;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.stock.management.domain.stock.service.StockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

	private final RedissonClient redissonClient;
	private final StockService stockService;

	/**
	 * pub-sub 기반이기에 레디스에 부하를 줄여주는 성능 이점이 존재.
	 * 하지만, 구현이 조금 복잡하거나 라이브러리의 의존성이 강해진다.
	 * redisson 은 자신이 점유하고 있는 락을 해제할 때 채널에 메세지를 보내줌으로서
	 * 락을 획득해야 하는 스레드들에게 락을 획득하라 전달해야한다.
	 * 그러면 락 획득을 해야하는 스레드들은 메세지를 받았을때 락 획득을 시도한다.
	 * lettuce 는 계속 락 획득을 시도하는 반면 redisson 은 락 해제가 되었을때 한번 혹은 몇번만 시도를 하게되니 redis에 부하를 줄여준다.
	 * @param id
	 * @param quantity
	 */
	public void decrease(Long id, Long quantity) {
		RLock lock = redissonClient.getLock(id.toString()); // 락 객체 획득

		try {
			// n초동안 락 획득을 시도할 것인지, n초동안 락을 점유할 것인지 설정
			// 테스트 실패 시 락 획득 시간을 늘려주거나 한다.
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

			if (!available) {
				log.info("lock 획득 실패 {}", lock.getName());
				return;
			}
			stockService.decreaseQuantity(id, quantity);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			// lock 해제
			lock.unlock();
		}
	}
}
