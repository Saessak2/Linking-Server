package com.linking.sse.emitter_repository;

import com.linking.global.common.CustomEmitter;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GroupSseInMemoryRepository implements IGroupSseRepository {

    private final Map<Long, Set<CustomEmitter>> groupSubscriber = new ConcurrentHashMap<>();

    @Override
    public Set<CustomEmitter> findEmittersByKey(Long key) {

        Set<CustomEmitter> emitters = this.groupSubscriber.get(key);

        if (emitters == null) return null;
        return emitters;
    }

    @Override
    public CustomEmitter save(Long key, CustomEmitter customEmitter) {

        Set<CustomEmitter> emitters = this.findEmittersByKey(key);

        if (emitters == null) {
            emitters = Collections.synchronizedSet(new HashSet<>());
            emitters.add(customEmitter);
            this.groupSubscriber.put(key, emitters);

        } else {
            emitters.add(customEmitter);
        }
        return customEmitter;
    }

    @Override
    public boolean deleteEmitter(Long key, CustomEmitter customEmitter) {
        Set<CustomEmitter> emittersByKey = this.findEmittersByKey(key);
        return emittersByKey.remove(customEmitter);
    }

    @Override
    public Set<CustomEmitter> deleteAllByProject(Long key) {
        Set<CustomEmitter> emittersByKey = this.findEmittersByKey(key);
        groupSubscriber.remove(key);
        return emittersByKey;
    }
}
