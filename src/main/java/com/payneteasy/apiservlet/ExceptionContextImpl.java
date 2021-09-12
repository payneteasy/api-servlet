package com.payneteasy.apiservlet;

import javax.servlet.http.HttpServletResponse;

public class ExceptionContextImpl implements IExceptionContext {

    private HttpServletResponse response;

    public ExceptionContextImpl(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public HttpServletResponse getHttpResponse() {
        return response;
    }
}
