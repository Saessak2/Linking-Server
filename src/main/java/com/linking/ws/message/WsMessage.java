package com.linking.ws.message;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
public class WsMessage<T>  {

    @JsonProperty
    private int resType;

    @JsonProperty
    private T data;

    @Builder
    public WsMessage(int resType, T data) {
        this.resType = resType;
        this.data = data;
    }
}
