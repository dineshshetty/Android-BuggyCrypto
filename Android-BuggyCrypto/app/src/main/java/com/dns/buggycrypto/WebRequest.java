package com.dns.buggycrypto;

public class WebRequest {


    private String document_id;

    private String signed_document_id;


    public String getDocumentId() {
        return document_id;
    }

    public String getSignedDocumentID() {
        return signed_document_id;
    }

    public void setDocumentId(String document_id) {
        this.document_id = document_id;
    }

    public void setSignedDocumentId(String signed_document_id) {
        this.signed_document_id = signed_document_id;
    }

}
