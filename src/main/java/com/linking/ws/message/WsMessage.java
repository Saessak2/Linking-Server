package com.linking.ws.message;


import com.fasterxml.jackson.annotation.JsonProperty;

public class WsMessage<T>  {

    @JsonProperty
    private int resType;

    private int publishType;

    @JsonProperty
    private T data;

    public WsMessage(int resType, int publishType, T data) {
        this.resType = resType;
        this.publishType = publishType;
        this.data = data;
    }
}
