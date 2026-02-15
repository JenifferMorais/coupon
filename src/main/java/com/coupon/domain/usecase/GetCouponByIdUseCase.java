package com.coupon.domain.usecase;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.gateway.CouponGateway;

public class GetCouponByIdUseCase {
    private final CouponGateway couponGateway;

    public GetCouponByIdUseCase(CouponGateway couponGateway) {
        this.couponGateway = couponGateway;
    }

    public Coupon execute(String id) {
        return couponGateway.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
    }
}
