package com.stock.management.domain.stock.repository;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

	/**
	 * setNx(set if not exist) 를 활용한 방식으로, 기존 값 존재여부에 따라 구현
	 * final로 선언
	 */
	private final RedisTemplate<String, String> redisTemplate;

	// Lock 메서드
			// setNx, key = stockId, value="lock", timeout=3s
	public Boolean lock(Long key) {
		return redisTemplate
			.opsForValue()
			.setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
	}

	// Unlock 메서드
	public Boolean unlock(Long key) {
		return redisTemplate.delete(generateKey(key));
	}

	private String generateKey(Long key) {
		return key.toString();
	}
}
