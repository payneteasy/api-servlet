package com.payneteasy.apiservlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payneteasy.apiservlet.service.HelloServiceSample;
import com.payneteasy.apiservlet.service.RequestMessageSample;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GsonApiServletTest {

    @Test
    public void doPost() throws IOException {
        HelloServiceSample service = new HelloServiceSample();
        Gson               gson    = new GsonBuilder().setPrettyPrinting().create();
        GsonApiServlet     servlet = new GsonApiServlet<>(service::sayHello, RequestMessageSample.class, gson, (aException, aContext) -> aException.printStackTrace());

        HttpServletRequest  servletRequest  = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        StringWriter        stringWriter    = new StringWriter();
        PrintWriter         printWriter     = new PrintWriter(stringWriter);


        when(servletRequest.getReader()).thenReturn(new BufferedReader(new CharArrayReader("{ \"name\": \"Java\"}".toCharArray())));
        when(servletResponse.getWriter()).thenReturn(printWriter);

        servlet.doPost(servletRequest, servletResponse);

        Mockito.verify(servletRequest).getReader();
        Mockito.verify(servletResponse).getWriter();

        Assert.assertEquals("{\n" +
                "  \"text\": \"Hello Java\"\n" +
                "}", stringWriter.getBuffer().toString());
    }
}