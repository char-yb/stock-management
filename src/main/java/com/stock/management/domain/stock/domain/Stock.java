package com.stock.management.domain.stock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long productId;

	private Long quantity;

	@Version
	private Integer version;

	@Builder(access = AccessLevel.PRIVATE)
	private Stock (
		Long productId,
		Long quantity
		// Long version
	) {
		this.productId = productId;
		this.quantity = quantity;
		// this.version = version;
	}

	public static Stock createStock(
		Long productId,
		Long quantity
	) {
		return Stock.builder()
			.productId(productId)
			.quantity(quantity)
			.build();
	}

	public void decrease(Long quantity) {
		if (this.quantity - quantity < 0) {
			throw new RuntimeException("재고는 0개 미만이 될 수 없습니다.");
		}

		this.quantity -= quantity;
	}
}
