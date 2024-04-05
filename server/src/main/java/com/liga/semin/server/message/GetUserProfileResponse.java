package com.liga.semin.server.message;

public record GetUserProfileResponse(
        long id,
        String name,
        String gender,
        byte[] image
) {}
