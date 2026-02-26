package com.coupon.infrastructure.persistence.mapper;

import com.coupon.domain.entity.Coupon;
import com.coupon.domain.entity.CouponStatus;
import com.coupon.infrastructure.persistence.cache.CacheCouponDTO;
import com.coupon.infrastructure.persistence.entity.CouponEntity;

public final class CouponMapper {

    private CouponMapper() {}

    public static CouponEntity toEntity(Coupon coupon) {
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

    public static Coupon toDomain(CouponEntity entity) {
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

    public static CacheCouponDTO toCacheDTO(Coupon coupon) {
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

    public static Coupon fromCacheDTO(CacheCouponDTO dto) {
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
}
