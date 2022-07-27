package com.payneteasy.apiservlet;

public class TestRequestValidator implements IRequestValidator {

    @Override
    public <T> T validateRequest(T aRequest) {
        return aRequest;
    }
}
