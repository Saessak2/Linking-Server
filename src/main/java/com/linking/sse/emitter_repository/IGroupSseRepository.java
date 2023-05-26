package com.linking.sse.emitter_repository;

import com.linking.global.common.CustomEmitter;

import java.util.Set;

public interface IGroupSseRepository {

    CustomEmitter save(Long key, CustomEmitter customEmitter);

    /**
     *
     * @param key
     * @return null or Set<CustomEmitter>
     */
    Set<CustomEmitter> findEmittersByKey(Long key);

    boolean deleteEmitter(Long key, CustomEmitter customEmitter);

    Set<CustomEmitter> deleteAllByProject(Long projectId);
}
