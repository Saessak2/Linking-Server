package com.linking.page.service;

import com.linking.page.domain.TextInput;
import com.linking.page.dto.TextInputMessage;
import com.linking.page.persistence.ISnapshotInMemoryRepository;
import com.linking.page.persistence.ITexInputtInMemoryRepository;
import com.linking.page.persistence.PageContentInputRepoImpl;
import com.linking.page.persistence.PageContentSnapshotRepoImpl;
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

//    private final PageContentInputRepoImpl pageContentInputRepoImpl;
    private final PageContentSnapshotRepoImpl pageContentSnapshotRepoImpl;

    public void inputText(Map<String, Object> attributes, TextInputMessage textInputMessage) {

        int editorType = textInputMessage.getEditorType();

        if (editorType == PAGE_CONTENT) {

//            TextInput textInput = toTextInput(attributes, textInputMessage);
//            Queue<TextInput> queue = pageContentInputRepoImpl.save((Long) attributes.get("pageId"), textInput);

            // todo snapshot 을 저장한다.
            pageContentSnapshotRepoImpl.save(textInputMessage.getDocs());

            // todo message.getDocs의 docs를 비교하여 변경된 첫번째 인덱스를 구하고 변경된 문자열 또는 삭제여부를 구한다.
            String docs = pageContentSnapshotRepoImpl.poll();
            if (docs == null) return;
            compareString(docs, textInputMessage.getDocs());

            // todo 다른 참여자에게 index, inputType, 문자열(insert경우)을 전송한다.

            // todo shapshot을 db에 일정 주기 마다 저장한다.
            // todo 페이지 조회 요청이 온 경우 즉시 shapshot 을 db에 저장후 페이지 조회 로직을 실행한다.

        } else if (editorType == BLOCK_TITLE) {

        } else if (editorType == BLOCK_CONTENT) {

        } else {
            return;
        }
    }

//    private TextInput toTextInput(Map<String, Object> attributes, TextInputMessage textInputMessage) {
//
//    }

    private void compareString(String oldStr, String newStr) {
        int index = 0;
        String changedString = "";
        String operation = "";

        for (int i = 0; i < oldStr.length(); i++) {
            if (oldStr.charAt(i) != newStr.charAt(i)) {
                index = i;
                changedString = newStr.charAt(i) + "";
                operation = "삽입";
                break;
            }
        }

        if (index == oldStr.length()) {
            operation = "삭제";
        }

        System.out.println("{" + index + ", " + changedString + ", " + operation + "}");
    }
}
