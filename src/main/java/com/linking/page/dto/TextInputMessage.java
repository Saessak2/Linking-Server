package com.linking.page.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TextInputMessage {

    @NotNull
    private Integer editorType;

    private Long blockId;

    @NotNull
    private String docs;

    public TextInputMessage(Integer editorType, Long blockId, String docs) {
        this.editorType = editorType;
        this.blockId = blockId;
        this.docs = docs;
    }


//    @NotNull
//    private Integer editorType;
//
//    private Long blockId;
//
//    @NotNull
//    private String inputType;
//
//    @NotNull
//    private Integer index;
//
//    private String character;
//
//    public TextInputMessage(Integer editorType, Long blockId, String inputType, Integer index, String character) {
//        this.editorType = editorType;
//        this.blockId = blockId;
//        this.inputType = inputType;
//        this.index = index;
//        this.character = character;
//    }
}
