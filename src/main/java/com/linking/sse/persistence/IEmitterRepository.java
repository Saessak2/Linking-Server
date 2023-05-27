package com.linking.sse.persistence;

import com.linking.sse.domain.CustomEmitter;

import java.util.Set;

public interface IEmitterRepository {

    CustomEmitter save(Long key, CustomEmitter customEmitter);

    /**
     *
     * @param key
     * @return null or Set<CustomEmitter>
     */
    Set<CustomEmitter> findEmittersByKey(Long key);

    boolean deleteEmitter(Long key, CustomEmitter customEmitter);

    Set<CustomEmitter> deleteAllByKey(Long projectId);
}
