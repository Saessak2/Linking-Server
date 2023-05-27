package com.linking.page.persistence;

import java.util.List;
import java.util.Queue;

public interface ISnapshotInMemoryRepository {

    void save(String docs);

    String poll();

    Queue<String> getDocs();

    void clear();
}
