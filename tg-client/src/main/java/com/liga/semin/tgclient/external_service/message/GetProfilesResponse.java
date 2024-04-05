package com.liga.semin.tgclient.external_service.message;

import java.util.List;

public record GetProfilesResponse(
        List<ProfileDto> profiles
) {
}
