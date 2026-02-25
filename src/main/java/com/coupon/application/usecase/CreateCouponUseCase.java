package com.coupon.application.usecase;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.gateway.CouponGateway;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class CreateCouponUseCase {
    private final CouponGateway couponGateway;

    public CreateCouponUseCase(CouponGateway couponGateway) {
        this.couponGateway = couponGateway;
    }

    public Coupon execute(String code, String description, String discountValue,
                          LocalDateTime expirationDate, Boolean published) {
        boolean isPublished = published != null && published;

        Coupon coupon = new Coupon(code, description, discountValue, expirationDate, isPublished);

        if (couponGateway.existsByCode(coupon.getCodeValue())) {
            log.warn("Coupon code already exists: {}", coupon.getCodeValue());
            throw new IllegalStateException("Coupon with code '" + coupon.getCodeValue() + "' already exists. Only the first 6 alphanumeric characters are considered");
        }

        Coupon saved = couponGateway.save(coupon);
        log.info("Coupon created successfully with id: {}", saved.getId());
        return saved;
    }
}
