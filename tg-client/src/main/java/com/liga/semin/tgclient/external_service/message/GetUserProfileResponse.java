package com.liga.semin.tgclient.external_service.message;

public record GetUserProfileResponse(
        String name,
        String gender,
        byte[] image
) {}
