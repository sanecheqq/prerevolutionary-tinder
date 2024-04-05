package com.liga.semin.tgclient.temporary_storage;

import com.liga.semin.tgclient.external_service.message.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TemporaryFavoritesStorageImpl implements TemporaryFavoritesStorage {
    private final Map<Long, List<ProfileDto>> usersProfiles = new HashMap<>();
    private final Map<Long, Integer> offsets = new HashMap<>();


    public void addProfiles(long id, List<ProfileDto> list) {
        usersProfiles.put(id, list);
        offsets.put(id, 0);
    }

    public List<ProfileDto> getProfiles(long id) {
        return usersProfiles.get(id);
    }

    public int getOffset(long id) {
        return offsets.get(id);
    }

    public void putOffset(long id, int offset) {
        offsets.put(id, offset);
    }
}
