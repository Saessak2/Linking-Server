package com.linking.page.persistence;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class PageContentSnapshotRepoImpl {

    private final Queue<String> document = new ConcurrentLinkedQueue<>();


    public void save(String docs) {
        document.add(docs);
    }

    public String poll() {
        return document.poll();
    }

    public Queue<String> getDocs() {
        return document;
    }

    public void clear() {
        document.clear();
    }
}
