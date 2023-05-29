package com.linking.socket.page.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class PageContentSnapshotRepoImpl{

    /**
     * key : pageId
     */
//    private final Map<Long, Queue<String>> document = new ConcurrentHashMap<>();
//
//    public void put(Long pageId, String doc) {
//        Queue queue = new ConcurrentLinkedQueue();
//        queue.add(doc);
//        document.put(pageId, queue);
//    }
//
//    public int mapSize() {
//        return document.size();
//    }
//
//    public int add(Long pageId, String docs) {
//        Queue<String> strings = document.get(pageId);
//        strings.add(docs);
//        return document.size();
//    }
//
//    public String poll(Long pageId) {
//        Queue<String> strings = document.get(pageId);
//        if (strings.size() == 1)
//            return strings.peek();
//        return strings.poll();
//    }
//
//    public String peek(Long pageId) {
//        return document.get(pageId).peek();
//    }
//
//    public int sizeByPage(Long pageId) {
//        return document.get(pageId).size();
//    }
//
//    public void clear() {
//        document.clear();
//    }

    private final Map<Long, String> document = new ConcurrentHashMap<>();

    // todo 페이지 생성 or 어플리케이션 초기화
    public void put(Long pageId, String doc) {
        document.put(pageId, doc);
    }

    public int mapSize() {
        return document.size();
    }

    public void clear() {
        document.clear();
    }

    public void replace(Long pageId, String doc) {
        document.replace(pageId, doc);
    }

    public String getDoc(Long pageId) {
        return document.get(pageId);
    }
}
