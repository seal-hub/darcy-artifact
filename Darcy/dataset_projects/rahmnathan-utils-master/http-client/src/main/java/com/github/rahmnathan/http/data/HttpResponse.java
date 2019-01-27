package com.github.rahmnathan.http.data;

public class HttpResponse {
    private final int responseCode;
    private final String responseMessage;

    public HttpResponse(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public static class Builder {
        private int responseCode;
        private String responseMessage;

        public static Builder newInstance(){
            return new Builder();
        }

        public Builder setResponseCode(int responseCode) {
            this.responseCode = responseCode;
            return this;
        }

        public Builder setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
            return this;
        }

        public HttpResponse build(){
            return new HttpResponse(responseCode, responseMessage);
        }
    }
}
