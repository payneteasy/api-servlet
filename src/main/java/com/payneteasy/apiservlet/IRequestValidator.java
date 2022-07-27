package com.payneteasy.apiservlet;

public interface IRequestValidator {

    <T> T validateRequest(T aRequest);

}
