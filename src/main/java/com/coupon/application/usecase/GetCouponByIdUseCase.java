package com.coupon.application.usecase;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.entity.CouponStatus;
import com.coupon.domain.gateway.CouponGateway;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

@Slf4j
public class GetCouponByIdUseCase {
    private final CouponGateway couponGateway;

    public GetCouponByIdUseCase(CouponGateway couponGateway) {
        this.couponGateway = couponGateway;
    }

    public Coupon execute(String id) {
        log.info("Fetching coupon by id: {}", id);
        Coupon coupon = couponGateway.findById(id)
                .orElseThrow(() -> {
                    log.warn("Coupon not found with id: {}", id);
                    return new NoSuchElementException("Coupon not found");
                });

        if (coupon.getStatus() == CouponStatus.DELETED) {
            log.warn("Coupon with id: {} is deleted", id);
            throw new NoSuchElementException("Coupon is not active");
        }

        return coupon;
    }
}
