package com.payneteasy.apiservlet;

import com.google.gson.Gson;
import com.payneteasy.apiservlet.logger.ILoggerContextFactory;
import org.eclipse.jetty.ee8.servlet.ServletContextHandler;
import org.eclipse.jetty.ee8.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;

import static com.payneteasy.apiservlet.logger.noop.LoggerContextFactoryNoop.NOOP_CONTEXT_FACTORY;

public class GsonJettyContextHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GsonJettyContextHandler.class);

    private final ServletContextHandler context;
    private final Gson                  gson;
    private final IExceptionHandler     exceptionHandler;
    private final IRequestValidator     requestValidator;
    private final ILoggerContextFactory loggerContextFactory;

    public GsonJettyContextHandler(ServletContextHandler aContext, Gson aGson, IExceptionHandler aHandler, IRequestValidator aValidator) {
        this(aContext, aGson, aHandler, aValidator, NOOP_CONTEXT_FACTORY);
    }

    public GsonJettyContextHandler(ServletContextHandler aContext, Gson aGson, IExceptionHandler aHandler, IRequestValidator aValidator, ILoggerContextFactory aLoggerFactory) {
        context = aContext;
        gson = aGson;
        exceptionHandler = aHandler;
        requestValidator = aValidator;
        loggerContextFactory = aLoggerFactory;
    }

    public void add(String path, HttpServlet servlet) {
        LOG.info("Adding servlet {}", path);
        context.addServlet(new ServletHolder(servlet), path);
    }

    public <I,O> void addApi(String path, IApiCall<I, O> aApiCall, Class<I> aRequestClass) {
        add(path, new GsonApiServlet<>(aApiCall, aRequestClass, gson, exceptionHandler, requestValidator, loggerContextFactory));
    }

}
