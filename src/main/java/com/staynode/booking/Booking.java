package com.staynode.booking;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    public enum Status { PENDING, CONFIRMED, CANCELLED, COMPLETED }
    public enum RoomType { SINGLE, DOUBLE, TRIPLE, SHARED }

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "property_id", nullable = false, length = 36)
    private String propertyId;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private RoomType room;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private Status status;

    private Integer amount;
    private Integer deposit;
    private String paymentId;
    private Instant paidAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = "BK_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (createdAt == null) createdAt = Instant.now();
        if (status == null) status = Status.PENDING;
        if (room == null) room = RoomType.SINGLE;
    }
}
