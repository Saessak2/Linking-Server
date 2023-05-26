package com.linking.page.persistence;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class PageContentSnapshotRepoImpl implements IDocsInMemoryRepository{

    private final List<String> document = new CopyOnWriteArrayList<>();


    @Override
    public void save(int index, String character) {
        document.add(index, character);
    }

    @Override
    public void delete(int index) {
        document.remove(index);
    }

    @Override
    public List<String> getDocs() {
        return document;
    }

    @Override
    public void clear() {
        document.clear();
    }
}
