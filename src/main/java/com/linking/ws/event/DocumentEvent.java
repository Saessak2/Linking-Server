package com.linking.ws.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DocumentEvent<T> {

    private int resType;
    private Long userId;
    private Long projectId;
    private T data;

    @Builder
    public DocumentEvent(int resType, Long userId, Long projectId, T data) {
        this.resType = resType;
        this.userId = userId;
        this.projectId = projectId;
        this.data = data;
    }
}
