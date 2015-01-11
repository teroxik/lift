package com.mj.lift.rest;

public class RestResponse {

    private Integer code;
    private String responseBody;

    public RestResponse(Integer code, String responseBody) {
        this.code = code;
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.setResponseBody(responseBody);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
