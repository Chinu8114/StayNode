package com.staynode.property;

import com.staynode.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyRepository repo;

    @GetMapping("/featured")
    public Map<String, Object> featured() {
        List<PropertyDto> list = repo.findByFeaturedTrue().stream().map(PropertyDto::from).toList();
        return Map.of("properties", list);
    }

    @GetMapping
    public Map<String, Object> list(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Double minRating
    ) {
        Property.Type t = parseEnum(Property.Type.class, type);
        Property.Gender g = parseEnum(Property.Gender.class, gender);
        List<PropertyDto> list = repo.search(emptyToNull(city), t, g, minPrice, maxPrice, minRating)
                .stream().map(PropertyDto::from).toList();
        return Map.of("properties", list, "count", list.size());
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable String id) {
        Property p = repo.findById(id).orElseThrow(() -> ApiException.notFound("Property not found"));
        return Map.of("property", PropertyDto.from(p));
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> cls, String v) {
        if (v == null || v.isBlank()) return null;
        try { return Enum.valueOf(cls, v); } catch (IllegalArgumentException e) { return null; }
    }
    private static String emptyToNull(String s) { return s == null || s.isBlank() ? null : s; }
}
