package com.staynode.user;

public record UserDto(
        String id,
        String _id,
        String name,
        String email,
        String phone,
        String role,
        String avatar
) {
    public static UserDto from(User u) {
        return new UserDto(u.getId(), u.getId(), u.getName(), u.getEmail(), u.getPhone(),
                u.getRole().name(), u.getAvatar());
    }
}
