package com.mj.lift.rest;

import org.json.JSONObject;

public class RestResponse {

    private Integer code;
    private JSONObject responseBody;

    public RestResponse(Integer code, JSONObject responseBody) {
        this.code = code;
        this.responseBody = responseBody;
    }

    public JSONObject getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(JSONObject responseBody) {
        this.setResponseBody(responseBody);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
