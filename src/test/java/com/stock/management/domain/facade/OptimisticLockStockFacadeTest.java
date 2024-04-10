package com.stock.management.domain.facade;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
class OptimisticLockStockFacadeTest {

	@Autowired
	private OptimisticLockStockFacade optimisticLockStockFacadeService;
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

	/**
	 * OptimisticLock 장점은 별도의 락을 잡지 않으므로 PessimisticLock보다 성능상 이점이 있다.
	 * 단점으로는 갱신 실패 시 재시도 로직을 개발자가 직접 작성해야한다.
	 * 빈번하게 일어나지 않을거라 예상되면 사용.
	 * @throws InterruptedException
	 */
	@Test
	public void 동시에_100개의_요청이_들어온다() throws InterruptedException {
		int threadCount = 100;

		// 비동기로 실행하는 작업을 단순화하여 사용할 수 있게 도와준다. JAVA API
		ExecutorService executorService = Executors.newFixedThreadPool(32);

		// 100개의 요청이 끝날때까지 기다려야 하므로 CountDownLatch 활용
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 1; i <= threadCount; i++) {
			executorService.submit(() -> {
				try {
					optimisticLockStockFacadeService.decrease(1L, 1L);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		Stock stock = stockRepository.findById(1L).orElseThrow(() -> new RuntimeException("존재하지 않는 재고입니다."));
		// 100 - (1 * 100) = 0
		assertEquals(0, stock.getQuantity());
	}
}