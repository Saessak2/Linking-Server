package com.linking.page.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
@Repository
public class PageContentSnapshotRepoImpl {

    private final Deque<String> document = new ConcurrentLinkedDeque<>();

    public int add(String docs) {
        log.info("add ------ {}", Thread.currentThread().getName());


        document.add(docs);
        return document.size();
    }

    public String pollAndClear() {

        log.info("pollAndClear ---- {}", Thread.currentThread().getName());

        String result = document.removeLast();
        clear();
        return result;
    }

    public Queue<String> getDocs() {
        return document;
    }

    public void clear() {
        document.clear();
    }
}
