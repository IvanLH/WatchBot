package com.legacy.apppolicia.common;

/**
 * Created by ivanl on 11/03/2017.
 */

public class Message {
    private int code;
    private String reference;
    private String verbose;

    public Message(int code, String reference, String verbose) {
        this.code = code;
        this.reference = reference;
        this.verbose = verbose;
    }

    public int getCode() {
        return code;
    }

    public String getReference() {
        return reference;
    }

    public String getVerbose() {
        return verbose;
    }
}
