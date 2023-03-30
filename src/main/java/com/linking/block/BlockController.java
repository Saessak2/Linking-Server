package com.linking.block;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;
}
