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
    private String character;
    @NotNull
    private Timestamp timestamp;

    public TextInput(String sessionId, String inputType, int index, String character) {
        this.sessionId = sessionId;
        this.inputType = inputType;
        this.index = index;
        this.character = character;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
    }
}
