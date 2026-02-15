package com.coupon.domain.usecase;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.gateway.CouponGateway;

public class DeleteCouponUseCase {
    private final CouponGateway couponGateway;

    public DeleteCouponUseCase(CouponGateway couponGateway) {
        this.couponGateway = couponGateway;
    }

    public void execute(String id) {
        Coupon coupon = couponGateway.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));

        coupon.delete();
        couponGateway.save(coupon);
    }
}
