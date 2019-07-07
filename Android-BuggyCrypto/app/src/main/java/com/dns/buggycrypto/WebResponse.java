package com.dns.buggycrypto;

public class WebResponse {


    protected String message;
    protected Boolean success = true;


    public String getWebRequestMessage() {
        return message;
    }


    public boolean getSuccess() {
        return success;

    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setWebRequestMessage(String message) {
        this.message = message;
    }


}
