package com.coupon.infrastructure.persistence.gateway;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.gateway.CouponGateway;
import com.coupon.infrastructure.persistence.entity.CouponEntity;
import com.coupon.infrastructure.persistence.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CachePut(value = "coupons", key = "#result.id")
    public Coupon save(Coupon coupon) {
        log.debug("Saving coupon with id: {}", coupon.getId());
        CouponEntity entity = toEntity(coupon);
        CouponEntity saved = repository.save(entity);
        log.debug("Coupon saved with id: {}", saved.getId());
        return toDomain(saved);
    }

    @Override
    @Cacheable(value = "coupons", unless = "#result == null || #result.isEmpty()")
    public Optional<Coupon> findById(String id) {
        log.debug("Finding coupon by id: {}", id);
        Optional<Coupon> result = repository.findById(id).map(this::toDomain);
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

    private CouponEntity toEntity(Coupon coupon) {
        return new CouponEntity(
                coupon.getId(),
                coupon.getCodeValue(),
                coupon.getDescription(),
                coupon.getDiscountValueAmount(),
                coupon.getExpirationDateValue(),
                coupon.getStatus(),
                coupon.isPublished(),
                coupon.isRedeemed(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt()
        );
    }

    private Coupon toDomain(CouponEntity entity) {
        return Coupon.reconstitute(
                entity.getId(),
                entity.getCode(),
                entity.getDescription(),
                entity.getDiscountValue().toString(),
                entity.getExpirationDate(),
                entity.getStatus(),
                entity.isPublished(),
                entity.isRedeemed(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
