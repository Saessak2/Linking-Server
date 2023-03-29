package com.linking.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageService {

    private final PageRepository pageRepository;

    @Autowired
    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }
}
