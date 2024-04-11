package com.stock.management.domain.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.management.domain.stock.domain.Stock;

/**
 * namedLock은 이름을 가진 메타데이터 락이다.
 * 이름을 가진 락을 획득한 후 해제할 때까지 다른 세션은 이 락을 사용할 수 없다.
 * 주의할 점으로는 트랜잭션이 종료될 때 락이 자동으로 해제하지 않기 때문에 별도의 명령어(release_lock)으로 해결해야한다.
 */
// 실무에서는 별로의 JDBC를 사용해야한다.
public interface LockRepository extends JpaRepository<Stock, Long> {
	// 3000은 timeout이다.
	@Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
	void getLock(String key);

	@Query(value = "select release_lock(:key)", nativeQuery = true)
	void releaseLock(String key);
}
