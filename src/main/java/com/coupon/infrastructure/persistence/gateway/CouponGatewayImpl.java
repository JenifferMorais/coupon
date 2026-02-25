package com.coupon.infrastructure.persistence.gateway;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.entity.CouponStatus;
import com.coupon.domain.gateway.CouponGateway;
import com.coupon.infrastructure.persistence.cache.CacheCouponDTO;
import com.coupon.infrastructure.persistence.entity.CouponEntity;
import com.coupon.infrastructure.persistence.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CouponGatewayImpl implements CouponGateway {
    private static final String CACHE_NAME = "coupons";

    private final CouponRepository repository;
    @Nullable
    private final CacheManager cacheManager;

    public CouponGatewayImpl(CouponRepository repository, @Nullable CacheManager cacheManager) {
        this.repository = repository;
        this.cacheManager = cacheManager;
    }

    @Override
    public Coupon save(Coupon coupon) {
        log.debug("Saving coupon with id: {}", coupon.getId());
        CouponEntity entity = toEntity(coupon);
        CouponEntity saved = repository.save(entity);
        Coupon result = toDomain(saved);
        log.debug("Coupon saved with id: {}", saved.getId());
        putInCache(result);
        return result;
    }

    @Override
    public Optional<Coupon> findById(String id) {
        log.debug("Finding coupon by id: {}", id);
        Coupon cached = getFromCache(id);
        if (cached != null) {
            log.debug("Cache hit for coupon id: {}", id);
            return Optional.of(cached);
        }
        log.debug("Cache miss for coupon id: {}", id);
        Optional<Coupon> result = repository.findById(id).map(this::toDomain);
        result.ifPresent(this::putInCache);
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

    private void putInCache(Coupon coupon) {
        if (cacheManager == null) return;
        try {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                cache.put(coupon.getId(), toCacheDTO(coupon));
            }
        } catch (Exception e) {
            log.warn("Failed to put coupon in cache, id: {}, error: {}", coupon.getId(), e.getMessage());
        }
    }

    private Coupon getFromCache(String id) {
        if (cacheManager == null) return null;
        try {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                CacheCouponDTO dto = cache.get(id, CacheCouponDTO.class);
                if (dto != null) {
                    return fromCacheDTO(dto);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to get coupon from cache, id: {}, error: {}", id, e.getMessage());
        }
        return null;
    }

    private CacheCouponDTO toCacheDTO(Coupon coupon) {
        return new CacheCouponDTO(
                coupon.getId(),
                coupon.getCodeValue(),
                coupon.getDescription(),
                coupon.getDiscountValueAmount(),
                coupon.getExpirationDateValue(),
                coupon.getStatus().name(),
                coupon.isPublished(),
                coupon.isRedeemed(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt()
        );
    }

    private Coupon fromCacheDTO(CacheCouponDTO dto) {
        return Coupon.reconstitute(
                dto.getId(),
                dto.getCode(),
                dto.getDescription(),
                dto.getDiscountValue().toString(),
                dto.getExpirationDate(),
                CouponStatus.valueOf(dto.getStatus()),
                dto.isPublished(),
                dto.isRedeemed(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
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
