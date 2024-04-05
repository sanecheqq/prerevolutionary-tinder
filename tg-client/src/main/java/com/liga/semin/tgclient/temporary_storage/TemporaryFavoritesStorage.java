package com.liga.semin.tgclient.temporary_storage;

import com.liga.semin.tgclient.external_service.message.ProfileDto;

import java.util.List;

public interface TemporaryFavoritesStorage {
    public void addProfiles(long id, List<ProfileDto> list);

    public List<ProfileDto> getProfiles(long id);

    public int getOffset(long id);

    public void putOffset(long id, int offset);
}
