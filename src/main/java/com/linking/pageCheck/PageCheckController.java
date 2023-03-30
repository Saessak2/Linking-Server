package com.linking.pageCheck;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/pagechecks")
@RequiredArgsConstructor
public class PageCheckController {

    private final PageCheckService pageCheckService;
}
