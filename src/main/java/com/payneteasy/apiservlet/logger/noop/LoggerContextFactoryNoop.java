package com.payneteasy.apiservlet.logger.noop;

import com.payneteasy.apiservlet.logger.ILoggerContext;
import com.payneteasy.apiservlet.logger.ILoggerContextFactory;
import com.payneteasy.apiservlet.logger.RequestLoggerEvent;
import com.payneteasy.apiservlet.logger.ResponseLoggerEvent;

public class LoggerContextFactoryNoop implements ILoggerContextFactory {

    public static final ILoggerContextFactory NOOP_CONTEXT_FACTORY = new LoggerContextFactoryNoop();
    public static final ILoggerContext        NOOP_CONTEXT         = new LoggerContextNoop();

    @Override
    public ILoggerContext createLoggerContext() {
        return NOOP_CONTEXT;
    }

    private static class LoggerContextNoop implements ILoggerContext {

        @Override
        public void onRequest(RequestLoggerEvent aEvent) {

        }

        @Override
        public void onSuccessResponse(ResponseLoggerEvent aEvent) {

        }

        @Override
        public void onErrorResponse(ResponseLoggerEvent aEvent, Throwable aException) {

        }
    }
}
