package com.linking.page.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextInput {

    @NotNull
    private String sessionId;
    @NotNull
    private String inputType;
    @NotNull
    private int index;
    private String docs;
    @NotNull
    private Timestamp timestamp;

    public TextInput(String sessionId, String inputType, int index, String docs) {
        this.sessionId = sessionId;
        this.inputType = inputType;
        this.index = index;
        this.docs = docs;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
    }
}
