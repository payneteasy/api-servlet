package com.payneteasy.apiservlet;

import com.google.gson.Gson;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;

public class GsonJettyContextHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GsonJettyContextHandler.class);

    private final ServletContextHandler context;
    private final Gson                  gson;

    public GsonJettyContextHandler(ServletContextHandler aContext, Gson aGson) {
        context = aContext;
        gson = aGson;
    }

    public void add(String path, HttpServlet servlet) {
        LOG.info("Adding servlet {}", path);
        context.addServlet(new ServletHolder(servlet), path);
    }

    public <I,O> void addApi(String path, IApiCall<I, O> aApiCall, Class<I> aRequestClass) {
        add(path, new GsonApiServlet<>(aApiCall, aRequestClass, gson));
    }

}
