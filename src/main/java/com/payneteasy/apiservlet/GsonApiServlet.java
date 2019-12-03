package com.payneteasy.apiservlet;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

public class GsonApiServlet<I, O> extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GsonApiServlet.class);

    private final IApiCall<I, O> apiCall;
    private final Class<I>       requestClass;
    private final Gson           gson;

    public GsonApiServlet(IApiCall<I, O> aApiCall, Class<I> aRequestClass, Gson aGson) {
        apiCall = aApiCall;
        gson = aGson;
        requestClass = aRequestClass;
    }

    @Override
    protected void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        try {
            String json    = toJsonString(aRequest.getReader());
            I      request = gson.fromJson(json, requestClass);

            LOG.debug("Got {} \n{}", aRequest.getRequestURI(), json);

            processRequest(aRequest, aResponse, request);
        } catch (IOException e) {
            sendError(aResponse, e);
        }
    }

    private void sendError(HttpServletResponse aResponse, IOException e) {
        String errorId = UUID.randomUUID().toString();
        LOG.error("{}: IO error", errorId, e);
        aResponse.setStatus(500);
        try {
            aResponse.getWriter().write("{'error' : 'IO error " + errorId + "'}");
        } catch (IOException e1) {
            LOG.error("Cannot write error to servlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        try {
            processRequest(aRequest, aResponse, null);
        } catch (IOException e) {
            sendError(aResponse, e);
        }
    }

    private void processRequest(HttpServletRequest aRequest, HttpServletResponse aResponse, I request) throws IOException {
        try {
            O response = apiCall.exec(request);

            aResponse.setContentType("application/json");
            gson.toJson(response, aResponse.getWriter());
        } catch (Exception e) {
            String id = UUID.randomUUID().toString();
            LOG.error("{}: Cannot process {}", aRequest.getRequestURI(), id, e);
            aResponse.setHeader("UNIQUE-ERROR-ID", id);
            aResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String toJsonString(BufferedReader aRequestReader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String        line;
        while ((line = aRequestReader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        return sb.toString();
    }
}
