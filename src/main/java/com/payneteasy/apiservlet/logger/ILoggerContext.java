package com.payneteasy.apiservlet.logger;

public interface ILoggerContext {

    void onRequest(RequestLoggerEvent aEvent);

    void onSuccessResponse(ResponseLoggerEvent aEvent);

    void onErrorResponse(ResponseLoggerEvent aEvent, Throwable aException);
}
