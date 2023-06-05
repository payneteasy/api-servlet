package com.payneteasy.apiservlet;

import com.payneteasy.apiservlet.logger.ILoggerContext;

import javax.servlet.http.HttpServletResponse;

public class ExceptionContextImpl implements IExceptionContext {

    private final HttpServletResponse response;
    private final ILoggerContext      loggerContext;

    public ExceptionContextImpl(HttpServletResponse response, ILoggerContext loggerContext) {
        this.response      = response;
        this.loggerContext = loggerContext;
    }

    @Override
    public HttpServletResponse getHttpResponse() {
        return response;
    }

    @Override
    public ILoggerContext getLoggerContext() {
        return loggerContext;
    }
}
