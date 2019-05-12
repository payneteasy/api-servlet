package com.payneteasy.apiservlet.service;

public class HelloServiceSample {

    public ResponseMessageSample sayHello(RequestMessageSample aName) {
        ResponseMessageSample response = new ResponseMessageSample();
        response.text = "Hello " + aName.name;
        return response;
    }
}
