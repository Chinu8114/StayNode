package com.staynode.wishlist;

import com.staynode.common.ApiException;
import com.staynode.property.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistRepository repo;
    private final PropertyRepository properties;

    @PostMapping("/properties/{propertyId}/wishlist")
    public Map<String, Object> toggle(@PathVariable String propertyId, Authentication auth) {
        if (auth == null) throw ApiException.unauthorized("Login required");
        if (!properties.existsById(propertyId)) throw ApiException.notFound("Property not found");

        String userId = auth.getPrincipal().toString();
        Wishlist.PK pk = new Wishlist.PK(userId, propertyId);
        boolean added;
        if (repo.existsById(pk)) {
            repo.deleteById(pk);
            added = false;
        } else {
            repo.save(new Wishlist(userId, propertyId, null));
            added = true;
        }
        return Map.of("success", true, "added", added);
    }

    @GetMapping("/wishlist")
    public Map<String, Object> list(Authentication auth) {
        if (auth == null) throw ApiException.unauthorized("Login required");
        String userId = auth.getPrincipal().toString();
        List<String> ids = repo.findByUserId(userId).stream().map(Wishlist::getPropertyId).toList();
        return Map.of("propertyIds", ids);
    }
}
