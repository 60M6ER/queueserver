package com.baikalsr.queueserver.jsonView;

public class ResponseStatus {
    private String status;

    public ResponseStatus() {
    }

    public ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
