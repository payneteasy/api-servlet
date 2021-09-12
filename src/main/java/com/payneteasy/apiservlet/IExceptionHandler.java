package com.payneteasy.apiservlet;

public interface IExceptionHandler {

    void handleException(Exception aException, IExceptionContext aContext);

}
