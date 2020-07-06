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
        System.out.println("");
        return message;
    }

    public String getRequestStatus() {
        return status;
    }

    public String getsigned_hash() {
        return signed_hash;
    }

    public String getstatus() {
        System.out.println("inside getstatus value of status = "+status);
        return status;
    }

    public String getmessage() {
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

    public void setWebRequestSignedHash(String signed_hash) {
        this.signed_hash = signed_hash;
    }


    public void setRequestStatus(String message) {
        this.status = status;
    }

    public void setsigned_hash(String signed_hash) {
        this.signed_hash = signed_hash;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public void setmessage(String message) {
        this.status = message;
    }



}
