package com.linking.page.persistence;

import com.linking.page.domain.TextInput;

import java.util.Queue;

public interface ITexInputtInMemoryRepository {

    /**
     * @param key
     * @param textInput
     * @return size of textInputQueueByKey
     */
    Queue<TextInput> save(Long key, TextInput textInput);

    Queue<TextInput> findAllByKey(Long key);

    boolean deleteAllByKey(Long key);

    boolean deleteAll();
}
