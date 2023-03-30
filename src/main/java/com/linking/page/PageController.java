package com.linking.page;

import com.linking.document.Document;
import com.linking.page.dto.PageReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "/pages")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewPage(@RequestBody PageReqDto pageReqDto) {
        pageService.createPage(pageReqDto);
        return "OK";
    }
}
