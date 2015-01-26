package com.mj.lift.server;

import org.json.JSONObject;

public class LiftServer {

    private String userId;
    private String sessionId;

    public LiftServer(String userId,String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public LiftServer(String userId) {
        this.userId = userId;
    }

    public LiftServer(){
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public enum Method {
        GET,
        PUT,
        DELETE,
        POST
    }

    public enum Api {
        USER_REGISTER,
        USER_LOGIN,
        USER_REGISTER_DEVICE,
        USER_GET_PUBLIC_PROFILE,
        USER_SET_PUBLIC_PROFILE,
        USER_CHECK_ACCOUNT,
        USER_GET_PROFILE_IMAGE,
        USER_SET_PROFILE_IMAGE,
        EXERCISE_GET_MUSLCE_GROUPS,
        EXERCISE_GET_EXERCISE_SESSION_SUMMARY,
        EXERCISE_GET_EXERCISE_SESSION_DATES,
        EXERCISE_GET_EXERCISE_SESSION,
        EXERCISE_DELETE_EXERCISE_SESSION,
        EXERCISE_SESSION_START,
        EXERCISE_SESSION_SUBMIT_DATA,
        EXERCISE_SESSION_END,
        EXERCISE_SESSION_ABANDON,
        EXERCISE_SESSION_REPLAY_START,
        EXERCISE_SESSION_REPLAY_DATA,
        EXERCISE_SESSION_GET_CLASSIFICATION_EXAMPLES,
        EXPLICIT_EXERCISE_CLASSIFICATION_START,
        EXPLICIT_EXERCISE_CLASSIFICATION_END;
    }



    public class LiftRequest {
        private String path;
        private Method method;
        private byte[] payload;
        private JSONObject jsonPayload;

        LiftRequest(String path, Method method) {
            this.path = path;
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public byte[] getPayload() {
            return payload;
        }

        public void setPayload(byte[] payload) {
            this.payload = payload;
        }

        public JSONObject getJsonPayload() {
            return jsonPayload;
        }

        public void setJsonPayload(JSONObject jsonPayload) {
            this.jsonPayload = jsonPayload;
        }
    }

    public LiftRequest createRequest(Api requestType) {
        switch (requestType) {
            case USER_REGISTER: return new LiftRequest("/user", Method.POST);
            case USER_LOGIN: return new LiftRequest("/user", Method.PUT);
            case USER_REGISTER_DEVICE: return new LiftRequest("/user/"+userId+"/device/android", Method.POST);
            case USER_GET_PUBLIC_PROFILE: return new LiftRequest("/user/"+userId, Method.GET);
            case USER_SET_PUBLIC_PROFILE: return new LiftRequest("/user/"+userId, Method.POST);
            case USER_CHECK_ACCOUNT: return new LiftRequest("/user/"+userId+"/check",Method.GET);
            case USER_GET_PROFILE_IMAGE: return new LiftRequest("/user/"+userId+"/image", Method.GET);
            case USER_SET_PROFILE_IMAGE: return new LiftRequest("/user/"+userId+"/image", Method.POST);
            case EXERCISE_GET_MUSLCE_GROUPS: return new LiftRequest("/exercise/musclegroups", Method.GET);
            //case EXERCISE_GET_EXERCISE_SESSION_SUMMARY: return new LiftRequest("/exercise/"+userId+"?date="+(Format.simpleDate(date))", Method.GET);
            case EXERCISE_GET_EXERCISE_SESSION_DATES: return new LiftRequest("/exercise/"+userId, Method.GET);
            case EXERCISE_GET_EXERCISE_SESSION: return new LiftRequest("/exercise/"+userId+"/"+sessionId, Method.GET);
            case EXERCISE_DELETE_EXERCISE_SESSION: return new LiftRequest("/exercise/"+userId+"/"+sessionId, Method.DELETE);
            case EXERCISE_SESSION_START: return new LiftRequest("/exercise/"+userId+"/start", Method.POST);
            case EXERCISE_SESSION_SUBMIT_DATA: return new LiftRequest("/exercise/"+userId+"/"+sessionId+"", Method.PUT);
            case EXERCISE_SESSION_END: return new LiftRequest("/exercise/"+userId+"/"+sessionId+"/end",  Method.POST);
            case EXERCISE_SESSION_ABANDON: return new LiftRequest("/exercise/"+userId+"/"+sessionId+"abandon", Method.POST);
            case EXERCISE_SESSION_REPLAY_START: return new LiftRequest("/exercise/"+userId+"/"+sessionId+"/replay", Method.POST);
            case EXERCISE_SESSION_REPLAY_DATA: return new LiftRequest("/exercise/"+userId+"/"+sessionId+"/replay", Method.PUT);
            case EXERCISE_SESSION_GET_CLASSIFICATION_EXAMPLES: return new LiftRequest("/exercise/"+userId+"/"+sessionId+"/classification", Method.GET);
            case EXPLICIT_EXERCISE_CLASSIFICATION_START: return new LiftRequest("/exercise/"+userId+"/classification", Method.POST);
            case EXPLICIT_EXERCISE_CLASSIFICATION_END: return new LiftRequest("/exercise/"+userId+"/classification",  Method.DELETE);
            default: return null;
        }

    }

}