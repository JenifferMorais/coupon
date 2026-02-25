package com.coupon.application.usecase;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.gateway.CouponGateway;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

@Slf4j
public class DeleteCouponUseCase {
    private final CouponGateway couponGateway;

    public DeleteCouponUseCase(CouponGateway couponGateway) {
        this.couponGateway = couponGateway;
    }

    public void execute(String id) {
        Coupon coupon = couponGateway.findById(id)
                .orElseThrow(() -> {
                    log.warn("Coupon not found for deletion with id: {}", id);
                    return new NoSuchElementException("Coupon not found");
                });

        coupon.delete();
        couponGateway.save(coupon);
        log.info("Coupon deleted with id: {}", id);
    }
}
