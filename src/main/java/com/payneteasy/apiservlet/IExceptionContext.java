package com.payneteasy.apiservlet;

import com.payneteasy.apiservlet.logger.ILoggerContext;

import javax.servlet.http.HttpServletResponse;

public interface IExceptionContext {

    HttpServletResponse getHttpResponse();

    ILoggerContext getLoggerContext();

}
