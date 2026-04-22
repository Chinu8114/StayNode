package com.staynode.booking;

import com.staynode.common.ApiException;
import com.staynode.property.Property;
import com.staynode.property.PropertyRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingRepository bookings;
    private final PropertyRepository properties;

    public record CreateReq(
            @NotNull String propertyId,
            @NotNull LocalDate checkIn,
            @NotNull LocalDate checkOut,
            String room
    ) {}

    @PostMapping
    public Map<String, Object> create(@Valid @RequestBody CreateReq req, Authentication auth) {
        if (auth == null) throw ApiException.unauthorized("Login required");
        if (!req.checkOut().isAfter(req.checkIn())) {
            throw ApiException.badRequest("checkOut must be after checkIn");
        }
        Property prop = properties.findById(req.propertyId())
                .orElseThrow(() -> ApiException.notFound("Property not found"));

        long months = Math.max(1, ChronoUnit.MONTHS.between(req.checkIn().withDayOfMonth(1),
                req.checkOut().withDayOfMonth(1)));
        int amount = (int) (prop.getPrice() * months);
        int deposit = prop.getPrice() * (prop.getDepositMonths() == null ? 2 : prop.getDepositMonths());

        Booking.RoomType room;
        try { room = req.room() == null ? Booking.RoomType.SINGLE : Booking.RoomType.valueOf(req.room().toUpperCase()); }
        catch (Exception e) { room = Booking.RoomType.SINGLE; }

        Booking b = Booking.builder()
                .userId(auth.getPrincipal().toString())
                .propertyId(req.propertyId())
                .checkIn(req.checkIn())
                .checkOut(req.checkOut())
                .room(room)
                .status(Booking.Status.PENDING)
                .amount(amount)
                .deposit(deposit)
                .build();
        bookings.save(b);
        return Map.of("booking", b);
    }

    @GetMapping
    public Map<String, Object> mine(Authentication auth) {
        if (auth == null) throw ApiException.unauthorized("Login required");
        String userId = auth.getPrincipal().toString();
        List<Booking> list = bookings.findByUserIdOrderByCreatedAtDesc(userId);
        return Map.of("bookings", list);
    }
}
