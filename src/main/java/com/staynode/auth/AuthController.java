package com.staynode.auth;

import com.staynode.common.ApiException;
import com.staynode.security.JwtService;
import com.staynode.user.User;
import com.staynode.user.UserDto;
import com.staynode.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public record RegisterReq(
            @NotBlank String name,
            @Email @NotBlank String email,
            @Size(min = 6) String password,
            String phone,
            String role
    ) {}

    public record LoginReq(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}

    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegisterReq req) {
        if (users.existsByEmailIgnoreCase(req.email())) {
            throw ApiException.conflict("Email already registered");
        }
        User.Role role;
        try {
            role = req.role() == null ? User.Role.USER : User.Role.valueOf(req.role().toUpperCase());
            if (role == User.Role.ADMIN) role = User.Role.USER; // never let clients self-register as admin
        } catch (IllegalArgumentException e) {
            role = User.Role.USER;
        }

        User u = User.builder()
                .name(req.name())
                .email(req.email().toLowerCase())
                .password(encoder.encode(req.password()))
                .phone(req.phone())
                .role(role)
                .build();
        users.save(u);

        String token = jwt.generate(u.getId(), u.getEmail(), u.getRole().name());
        return Map.of("user", UserDto.from(u), "token", token);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginReq req) {
        User u = users.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> ApiException.unauthorized("Invalid email or password"));
        if (!encoder.matches(req.password(), u.getPassword())) {
            throw ApiException.unauthorized("Invalid email or password");
        }
        String token = jwt.generate(u.getId(), u.getEmail(), u.getRole().name());
        return Map.of("user", UserDto.from(u), "token", token);
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            throw ApiException.unauthorized("Not authenticated");
        }
        String userId = auth.getPrincipal().toString();
        User u = users.findById(userId).orElseThrow(() -> ApiException.unauthorized("User not found"));
        return Map.of("user", UserDto.from(u));
    }
}
