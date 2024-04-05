package com.liga.semin.server.message;

public record GetUserProfileResponse(
   String name,
   String gender,
   byte[] image
) {}
