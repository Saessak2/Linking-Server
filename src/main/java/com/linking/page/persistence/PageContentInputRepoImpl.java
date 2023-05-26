package com.linking.page.persistence;

import com.linking.page.domain.TextInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Repository
public class PageContentInputRepoImpl implements ITexInputtInMemoryRepository {

    private final Map<Long, Queue<TextInput>> map = new ConcurrentHashMap<>();

    @Override
    public Queue<TextInput> save(Long key, TextInput textInput) {

        Queue<TextInput> queue = findAllByKey(key);

        if (queue == null) {
            queue = new ConcurrentLinkedQueue<>();
            queue.add(textInput);
            map.put(key, queue);

        } else {
            queue.add(textInput);
        }

        return queue;
    }

    @Override
    public Queue<TextInput> findAllByKey(Long key) {
        return map.get(key);
    }

    @Override
    public boolean deleteAllByKey(Long key) {
        if (map.remove(key) == null)
            return false;
        return true;
    }

    @Override
    public boolean deleteAll() {

        try {
            map.clear();
        } catch (UnsupportedOperationException exception) {
            System.out.println("");
            log.error("PageContentRepoImpl.deleteAll ==> exception : {}", exception.getMessage());
        }
        return true;
    }
}
