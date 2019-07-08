package com.dns.buggycrypto;

public class WebResponse {


    protected String message;
    protected Boolean success = true;
    protected String signed_hash;
    protected String status;



    public String getWebRequestMessage() {
        return message;
    }

    public String getWebRequestSignedHash() {
        return signed_hash;
    }

    public String getRequestStatus() {
        return status;
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

    public void setWebRequestSignedHash(String message) {
        this.signed_hash = signed_hash;
    }


    public void setRequestStatus(String message) {
        this.status = status;
    }




}
