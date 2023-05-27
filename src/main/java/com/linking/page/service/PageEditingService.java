package com.linking.page.service;

import com.linking.page.dto.TextInputMessage;
import com.linking.page.dto.TextOutputMessage;
import com.linking.page.dto.TextSendEvent;
import com.linking.page.persistence.PageContentSnapshotRepoImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageEditingService {

    private static final int PAGE_CONTENT = 0;
    private static final int BLOCK_TITLE = 1;
    private static final int BLOCK_CONTENT = 2;

//    private final PageContentInputRepoImpl pageContentInputRepoImpl;
    private final PageContentSnapshotRepoImpl pageContentSnapshotRepoImpl;
    private final ApplicationEventPublisher publisher;

    public void inputText(Map<String, Object> attributes, TextInputMessage textInputMessage) {

        log.info("inputText -------- {}", Thread.currentThread().getName());

        int editorType = textInputMessage.getEditorType();

        if (editorType == PAGE_CONTENT) {

//            TextInput textInput = toTextInput(attributes, textInputMessage);
//            Queue<TextInput> queue = pageContentInputRepoImpl.save((Long) attributes.get("pageId"), textInput);

            // todo snapshot 을 저장한다.
            pageContentSnapshotRepoImpl.add(textInputMessage.getDocs());

            TextSendEvent event = TextSendEvent.builder()
                    .sessionId((String) attributes.get("sessionId"))
                    .pageId((Long) attributes.get("pageId"))
                    .textOutputMessage(
                            TextOutputMessage.builder()
                                    .editorType(PAGE_CONTENT)
                                    .pageId((Long) attributes.get("pageId"))
                                    .blockId(-1L)
                                    .docs(pageContentSnapshotRepoImpl.pollAndClear())
                                    .build()
                    )
                    .build();

            publisher.publishEvent(event);


        } else if (editorType == BLOCK_TITLE) {

        } else if (editorType == BLOCK_CONTENT) {

        } else {
            return;
        }
    }

//    private TextInput toTextInput(Map<String, Object> attributes, TextInputMessage textInputMessage) {
//
//    }


}
