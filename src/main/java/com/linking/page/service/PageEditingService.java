package com.linking.page.service;

import com.linking.page.domain.TextInput;
import com.linking.page.dto.TextInputMessage;
import com.linking.page.persistence.IDocsInMemoryRepository;
import com.linking.page.persistence.ITexInputtInMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageEditingService {

    private static final int PAGE_CONTENT = 0;
    private static final int BLOCK_TITLE = 1;
    private static final int BLOCK_CONTENT = 2;

    private final ITexInputtInMemoryRepository pageContentInputRepoImpl;
    private final IDocsInMemoryRepository pageContentSnapshotRepoImpl;

    public void inputText(Map<String, Object> attributes, TextInputMessage textInputMessage) {

        int editorType = textInputMessage.getEditorType();


        if (editorType == PAGE_CONTENT) {

            TextInput textInput = toTextInput(attributes, textInputMessage);
            Queue<TextInput> queue = pageContentInputRepoImpl.save((Long) attributes.get("pageId"), textInput);


        } else if (editorType == BLOCK_TITLE) {

        } else if (editorType == BLOCK_CONTENT) {

        } else {
            return;
        }
    }

    private TextInput toTextInput(Map<String, Object> attributes, TextInputMessage textInputMessage) {

        return new TextInput(
                (String) attributes.get("sessionId"),
                textInputMessage.getInputType(),
                textInputMessage.getIndex(),
                textInputMessage.getCharacter()
        );
    }
}
