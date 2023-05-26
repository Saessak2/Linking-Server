package com.linking.page.persistence;

import java.util.List;

public interface IDocsInMemoryRepository {

    void save(int index, String character);

    void delete(int index);

    List<String> getDocs();

    void clear();
}
