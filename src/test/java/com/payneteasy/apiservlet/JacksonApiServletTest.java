package com.payneteasy.apiservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payneteasy.apiservlet.service.HelloServiceSample;
import com.payneteasy.apiservlet.service.RequestMessageSample;
import com.payneteasy.apiservlet.service.ResponseMessageSample;
import com.payneteasy.apiservlet.servlet.ServletInputStreamSample;
import com.payneteasy.apiservlet.servlet.ServletOutputStreamSample;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JacksonApiServletTest {

    @Test
    public void doPost() throws IOException {
        HelloServiceSample service = new HelloServiceSample();
        ObjectMapper       mapper  = new ObjectMapper();
        JacksonApiServlet  servlet = new JacksonApiServlet<>(service::sayHello, RequestMessageSample.class, ResponseMessageSample.class, mapper);

        HttpServletRequest        servletRequest  = mock(HttpServletRequest.class);
        HttpServletResponse       servletResponse = mock(HttpServletResponse.class);
        ServletOutputStreamSample outputStream    = new ServletOutputStreamSample(1024);

        when(servletRequest.getInputStream()).thenReturn(new ServletInputStreamSample("{ \"name\": \"Java\"}".getBytes()));
        when(servletResponse.getOutputStream()).thenReturn(outputStream);

        servlet.doPost(servletRequest, servletResponse);

        Mockito.verify(servletRequest).getInputStream();
        Mockito.verify(servletResponse).getOutputStream();

        Assert.assertEquals("{\"text\":\"Hello Java\"}", new String(outputStream.getBytes()));
    }
}