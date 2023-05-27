package com.linking.page.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Repository
public class PageContentSnapshotRepoImpl{

    /**
     * key : pageId
     */
    private final Map<Long, Queue<String>> document = new ConcurrentHashMap<>();

    public void put(Long pageId, String doc) {
        Queue queue = new ConcurrentLinkedQueue();
        queue.add(doc);
        document.put(pageId, queue);
    }

    public int mapSize() {
        return document.size();
    }

    public int add(Long pageId, String docs) {
        log.info("add ------ {}", Thread.currentThread().getName());
        Queue<String> strings = document.get(pageId);
        strings.add(docs);
        return document.size();
    }

    public String poll(Long pageId) {
        log.info("poll ---- {}", Thread.currentThread().getName());
        Queue<String> strings = document.get(pageId);
        if (strings.size() == 1)
            return strings.peek();
        return strings.poll();
    }

    public String peek(Long pageId) {
        return document.get(pageId).peek();
    }

    public int sizeByPage(Long pageId) {
        return document.get(pageId).size();
    }

    public void clear() {
        document.clear();
    }
}
