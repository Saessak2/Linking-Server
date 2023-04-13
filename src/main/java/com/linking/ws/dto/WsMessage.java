package com.linking.ws.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class WsMessage<T>  {

    @JsonProperty
    private int type;

    @JsonProperty
    private T data;

    public WsMessage(int type, T data) {
        this.type = type;
        this.data = data;
    }
}
