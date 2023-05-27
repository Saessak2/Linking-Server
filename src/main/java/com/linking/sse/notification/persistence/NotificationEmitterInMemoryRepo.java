package com.linking.sse.notification.persistence;

import com.linking.sse.domain.CustomEmitter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationEmitterInMemoryRepo {

    /**
     * key : projectId
     */
    private final Map<Long, Set<CustomEmitter>> notificationSubscriber = new ConcurrentHashMap<>();


    public CustomEmitter save(Long key, CustomEmitter customEmitter) {
        return null;
    }

    public Set<CustomEmitter> findEmittersByKey(Long key) {
        return null;
    }

    public boolean deleteEmitter(Long key, CustomEmitter customEmitter) {
        return false;
    }

    public Set<CustomEmitter> deleteAllByKey(Long projectId) {
        return null;
    }
}
