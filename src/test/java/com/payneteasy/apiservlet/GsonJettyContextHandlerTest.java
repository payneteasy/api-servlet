package com.payneteasy.apiservlet;

import com.google.gson.Gson;
import com.payneteasy.apiservlet.service.HelloServiceSample;
import com.payneteasy.apiservlet.service.RequestMessageSample;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;

public class GsonJettyContextHandlerTest {

    private final ServletContextHandler   context = new ServletContextHandler();
    private final GsonJettyContextHandler handler = new GsonJettyContextHandler(context, new Gson(), (aException, aContext) -> aException.printStackTrace(), new TestRequestValidator());

    @Test
    public void add() {
        handler.add("/test", new HttpServlet() {});
        Assert.assertEquals(1, context.getServletHandler().getServlets().length);
    }

    @Test
    public void addApi() {
        HelloServiceSample service = new HelloServiceSample();
        handler.addApi("/test-api", service::sayHello, RequestMessageSample.class);
        Assert.assertEquals(1, context.getServletHandler().getServlets().length);
    }
}