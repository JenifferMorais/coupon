package com.coupon.infrastructure.persistence.gateway;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.gateway.CouponGateway;
import com.coupon.infrastructure.persistence.cache.CacheCouponDTO;
import com.coupon.infrastructure.persistence.mapper.CouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Primary
public class CachingCouponGateway implements CouponGateway {
    private static final String CACHE_NAME = "coupons";

    private final CouponGatewayImpl delegate;
    @Nullable
    private final CacheManager cacheManager;

    public CachingCouponGateway(CouponGatewayImpl delegate, @Nullable CacheManager cacheManager) {
        this.delegate = delegate;
        this.cacheManager = cacheManager;
    }

    @Override
    public Coupon save(Coupon coupon) {
        Coupon result = delegate.save(coupon);
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
        Optional<Coupon> result = delegate.findById(id);
        result.ifPresent(this::putInCache);
        return result;
    }

    @Override
    public boolean existsByCode(String code) {
        return delegate.existsByCode(code);
    }

    private void putInCache(Coupon coupon) {
        if (cacheManager == null) return;
        try {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                cache.put(coupon.getId(), CouponMapper.toCacheDTO(coupon));
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
                    return CouponMapper.fromCacheDTO(dto);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to get coupon from cache, id: {}, error: {}", id, e.getMessage());
        }
        return null;
    }
}
