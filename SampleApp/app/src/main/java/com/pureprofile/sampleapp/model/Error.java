package com.pureprofile.sampleapp.model;

public class Error {
    public int statusCode;
    public String message;
    public Data data;

    public class Data {
        public String code;
    }
}
