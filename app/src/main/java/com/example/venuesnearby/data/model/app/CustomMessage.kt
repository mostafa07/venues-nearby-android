package com.example.venuesnearby.data.model.app;

public class CustomMessage {

    private int messageResourceId;
    private Object[] params;

    public CustomMessage(int messageResourceId, Object... params) {
        this.messageResourceId = messageResourceId;
        this.params = params;
    }

    public int getMessageResourceId() {
        return messageResourceId;
    }

    public void setMessageResourceId(int messageResourceId) {
        this.messageResourceId = messageResourceId;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
