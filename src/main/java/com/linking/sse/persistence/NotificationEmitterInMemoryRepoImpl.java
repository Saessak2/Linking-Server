package com.linking.sse.persistence;

import com.linking.sse.domain.CustomEmitter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationEmitterInMemoryRepoImpl implements IEmitterRepository{

    /**
     * key : projectId
     */
    private final Map<Long, Set<CustomEmitter>> notificationSubscriber = new ConcurrentHashMap<>();


    @Override
    public CustomEmitter save(Long key, CustomEmitter customEmitter) {
        return null;
    }

    @Override
    public Set<CustomEmitter> findEmittersByKey(Long key) {
        return null;
    }

    @Override
    public boolean deleteEmitter(Long key, CustomEmitter customEmitter) {
        return false;
    }

    @Override
    public Set<CustomEmitter> deleteAllByKey(Long projectId) {
        return null;
    }
}
