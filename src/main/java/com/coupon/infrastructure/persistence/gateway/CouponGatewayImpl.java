package com.coupon.infrastructure.persistence.gateway;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.gateway.CouponGateway;
import com.coupon.infrastructure.persistence.entity.CouponEntity;
import com.coupon.infrastructure.persistence.mapper.CouponMapper;
import com.coupon.infrastructure.persistence.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CouponGatewayImpl implements CouponGateway {

    private final CouponRepository repository;

    public CouponGatewayImpl(CouponRepository repository) {
        this.repository = repository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        log.debug("Saving coupon with id: {}", coupon.getId());
        CouponEntity entity = CouponMapper.toEntity(coupon);
        CouponEntity saved = repository.save(entity);
        Coupon result = CouponMapper.toDomain(saved);
        log.debug("Coupon saved with id: {}", saved.getId());
        return result;
    }

    @Override
    public Optional<Coupon> findById(String id) {
        log.debug("Finding coupon by id: {}", id);
        Optional<Coupon> result = repository.findById(id).map(CouponMapper::toDomain);
        log.debug("Coupon found by id {}: {}", id, result.isPresent());
        return result;
    }

    @Override
    public boolean existsByCode(String code) {
        log.debug("Checking if coupon exists by code: {}", code);
        boolean exists = repository.existsByCode(code);
        log.debug("Coupon exists by code {}: {}", code, exists);
        return exists;
    }
}
