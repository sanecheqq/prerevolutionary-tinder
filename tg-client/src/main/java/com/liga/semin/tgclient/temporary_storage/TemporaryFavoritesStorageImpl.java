package com.liga.semin.tgclient.temporary_storage;

import com.liga.semin.tgclient.external_service.message.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class TemporaryFavoritesStorageImpl implements TemporaryFavoritesStorage {
    private final Map<Long, List<ProfileDto>> usersProfiles = new HashMap<>();
    private final Map<Long, Integer> offsets = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TemporaryFavoritesStorageImpl.class);


    public void addProfiles(long id, List<ProfileDto> list) {
        logger.debug("Adding favorite/followers/mutual profiles for user {}", id);
        usersProfiles.put(id, list);
        offsets.put(id, 0);
    }

    public List<ProfileDto> getProfiles(long id) {
        logger.debug("Getting favorite/followers/mutual profiles for user {}", id);
        return usersProfiles.get(id);
    }


    public int getOffset(long id) {
        logger.debug("Getting offset for user {}", id);
        return offsets.get(id);
    }

    public void putOffset(long id, int offset) {
        logger.debug("Updating offset for user {}", id);
        offsets.put(id, offset);
    }
}
