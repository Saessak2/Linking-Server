package com.linking.block;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
}
