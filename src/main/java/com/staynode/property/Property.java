package com.staynode.property;

import com.staynode.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    public enum Type { PG, Hostel, Flat }
    public enum Gender { MALE, FEMALE, ANY }

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 512)
    private String location;

    private Double lat;
    private Double lng;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Gender gender;

    private Double rating;
    private Integer reviews;
    private Integer availSlots;

    @Column(length = 4000)
    private String description;

    private Integer depositMonths;

    private boolean featured;
    private boolean verified;
    private boolean isNew;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_amenities", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "amenity")
    @Builder.Default
    private List<String> amenities = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_rules", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "rule", length = 512)
    @Builder.Default
    private List<String> rules = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "image", length = 512)
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
        if (createdAt == null) createdAt = Instant.now();
        if (gender == null) gender = Gender.ANY;
        if (rating == null) rating = 4.5;
        if (reviews == null) reviews = 0;
        if (availSlots == null) availSlots = 0;
        if (depositMonths == null) depositMonths = 2;
    }
}
