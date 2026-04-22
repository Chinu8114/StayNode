package com.staynode.property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, String> {

    List<Property> findByFeaturedTrue();

    @Query("""
        SELECT DISTINCT p FROM Property p
        WHERE (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))
               OR LOWER(p.location) LIKE LOWER(CONCAT('%', :city, '%')))
          AND (:type IS NULL OR p.type = :type)
          AND (:gender IS NULL OR p.gender = :gender)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (:minRating IS NULL OR p.rating >= :minRating)
        """)
    List<Property> search(String city, Property.Type type, Property.Gender gender,
                          Integer minPrice, Integer maxPrice, Double minRating);
}
