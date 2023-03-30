package com.linking.page;

import com.linking.page.dto.PageReqDto;
import com.linking.page.dto.PageResDto;
import com.linking.util.CrudInterface;
import com.linking.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "/pages")
@RequiredArgsConstructor
public class PageController implements CrudInterface<PageReqDto, PageResDto> {

    private final PageService pageService;

    @Override
    public Message<PageResDto> create(Message<PageReqDto> request) {
        return null;
    }

    @Override
    public Message<PageResDto> read(Long id) {
        return null;
    }

    @Override
    public Message<PageResDto> update(Message<PageReqDto> request) {
        return null;
    }

    @Override
    public Message delete(Long id) {
        return null;
    }
}
