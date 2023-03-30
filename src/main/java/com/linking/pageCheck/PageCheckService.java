package com.linking.pageCheck;

import com.linking.page.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageCheckService {

    private final PageRepository pageRepository;

}
