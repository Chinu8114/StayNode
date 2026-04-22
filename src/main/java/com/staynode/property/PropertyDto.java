package com.staynode.property;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record PropertyDto(
        String _id,
        String id,
        String title,
        String city,
        String location,
        Double lat,
        Double lng,
        Integer price,
        String type,
        String gender,
        Double rating,
        Integer reviews,
        Integer availSlots,
        List<String> amenities,
        List<String> rules,
        List<String> images,
        String description,
        Integer depositMonths,
        boolean featured,
        boolean verified,
        boolean isNew,
        Map<String, Object> owner,
        Instant createdAt
) {
    public static PropertyDto from(Property p) {
        Map<String, Object> ownerMap = null;
        if (p.getOwner() != null) {
            ownerMap = Map.of(
                    "_id", p.getOwner().getId(),
                    "name", p.getOwner().getName() == null ? "" : p.getOwner().getName(),
                    "phone", p.getOwner().getPhone() == null ? "" : p.getOwner().getPhone()
            );
        }
        return new PropertyDto(
                p.getId(), p.getId(),
                p.getTitle(), p.getCity(), p.getLocation(),
                p.getLat(), p.getLng(),
                p.getPrice(),
                p.getType().name(),
                p.getGender().name(),
                p.getRating(), p.getReviews(), p.getAvailSlots(),
                p.getAmenities(), p.getRules(), p.getImages(),
                p.getDescription(), p.getDepositMonths(),
                p.isFeatured(), p.isVerified(), p.isNew(),
                ownerMap,
                p.getCreatedAt()
        );
    }
}
