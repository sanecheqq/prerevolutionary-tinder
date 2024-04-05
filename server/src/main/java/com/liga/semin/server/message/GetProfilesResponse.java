package com.liga.semin.server.message;

import java.util.List;

public record GetProfilesResponse(
        List<ProfileDto> profiles
) {
}
