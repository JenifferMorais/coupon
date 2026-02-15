package com.coupon.infrastructure.config;

import com.coupon.domain.gateway.CouponGateway;
import com.coupon.domain.usecase.CreateCouponUseCase;
import com.coupon.domain.usecase.DeleteCouponUseCase;
import com.coupon.domain.usecase.GetCouponByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CreateCouponUseCase createCouponUseCase(CouponGateway couponGateway) {
        return new CreateCouponUseCase(couponGateway);
    }

    @Bean
    public DeleteCouponUseCase deleteCouponUseCase(CouponGateway couponGateway) {
        return new DeleteCouponUseCase(couponGateway);
    }

    @Bean
    public GetCouponByIdUseCase getCouponByIdUseCase(CouponGateway couponGateway) {
        return new GetCouponByIdUseCase(couponGateway);
    }
}
