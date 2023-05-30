package com.linking.socket.page.persistence;

import com.linking.socket.page.BlockSnapshot;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BlockPageSnapshotRepo {

    /**
     * key : pageId
     * value : key : blockId
     */
    private final Map<Long, Map<Long, BlockSnapshot>> document = new ConcurrentHashMap<>();

    public void putPage(Long pageId) {
        document.put(pageId, new ConcurrentHashMap<>());
    }

    public void putBlock(Long pageId, Long blockId, BlockSnapshot blockSnapshot) {
        Map<Long, BlockSnapshot> page = document.get(pageId);
        page.put(blockId, blockSnapshot);
    }

    public boolean delete(Long pageId) {
        if(document.remove(pageId) != null) return true;
        return false;
    }

    public Map<Long, BlockSnapshot> findByPageId(Long pageId) {
        return document.get(pageId);
    }

    public int size() {
        return document.size();
    }
}
