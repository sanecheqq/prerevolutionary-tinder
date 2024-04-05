package com.liga.semin.tgclient.external_service.message;

public record GetUserProfileResponse(
        long id,
        String name,
        String gender,
        byte[] image
) {}
