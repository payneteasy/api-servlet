package com.payneteasy.apiservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class JacksonApiServlet<Request,Response> extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonApiServlet.class);

    private final IApiCall<Request,Response> apiCall;

    private final ObjectReader reader;
    private final ObjectWriter writer;

    public JacksonApiServlet(IApiCall<Request, Response> aApiCall, Class<Request> aRequestClass, Class<Response> aResponseClass, ObjectMapper aMapper) {
        this.apiCall = aApiCall;
        reader = aMapper.readerFor(aRequestClass);
        writer = aMapper.writerFor(aResponseClass);
    }

    @Override
    protected void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse) throws IOException {
        Request request = reader.readValue(aRequest.getInputStream());
        processRequest(aRequest, aResponse, request);
    }

    @Override
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws IOException {
        processRequest(aRequest, aResponse, null);
    }

    private void processRequest(HttpServletRequest aRequest, HttpServletResponse aResponse, Request request) throws IOException {
        try {
            Response response = apiCall.exec(request);
            aResponse.setContentType("application/json");
            writer.writeValue(aResponse.getOutputStream(), response);
        } catch (Exception e) {
            String id = UUID.randomUUID().toString();
            LOG.error("{}: Cannot process {}", aRequest.getRequestURI(), id, e);
            aResponse.setHeader("UNIQUE-ERROR-ID", id);
            aResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
