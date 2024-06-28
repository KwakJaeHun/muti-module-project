package com.jhkwak.orderservice.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Response {

    private boolean success;
    private int stateCode;
    private String description;

    public Response(ResponseCode responseCode){
        this.success = responseCode.getSuccess();
        this.stateCode = responseCode.getHttpStatusCode();
        this.description = responseCode.getMessage();
    }

}
