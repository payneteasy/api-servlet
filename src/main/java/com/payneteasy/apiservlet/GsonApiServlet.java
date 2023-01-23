package com.payneteasy.apiservlet;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class GsonApiServlet<I, O> extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GsonApiServlet.class);

    private final IApiCall<I, O>    apiCall;
    private final Class<I>          requestClass;
    private final Gson              gson;
    private final boolean           isVoidRequest;
    private final IExceptionHandler exceptionHandler;
    private final IRequestValidator requestValidator;

    public GsonApiServlet(IApiCall<I, O> aApiCall, Class<I> aRequestClass, Gson aGson, IExceptionHandler aExceptionHandler, IRequestValidator aRequestValidator) {
        apiCall = aApiCall;
        gson = aGson;
        requestClass = aRequestClass;
        isVoidRequest = VoidRequest.class.equals(aRequestClass);
        exceptionHandler = aExceptionHandler;
        requestValidator = aRequestValidator;
    }

    @Override
    protected void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        try {
            String json = toJsonString(aRequest.getReader());
            LOG.debug("Incoming json to {} is {}", aRequest.getRequestURI(), json);

            //noinspection unchecked
            I request = isVoidRequest ? (I) VoidRequest.VOID_REQUEST : gson.fromJson(json, requestClass);

            LOG.debug("Incoming POST message {} \n{}", aRequest.getRequestURI(), json);

            requestValidator.validateRequest(request, requestClass);

            processRequest(aRequest, aResponse, request);

        } catch (Exception e) {
            exceptionHandler.handleException(e, new ExceptionContextImpl(aResponse));
        }
    }

    @Override
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        try {
            LOG.debug("Incoming GET message {}", aRequest.getRequestURI());
            processRequest(aRequest, aResponse, null);
        } catch (Exception e) {
            exceptionHandler.handleException(e, new ExceptionContextImpl(aResponse));
        }
    }

    private void processRequest(HttpServletRequest aRequest, HttpServletResponse aResponse, I request) throws Exception {
        O      response     = apiCall.exec(request);
        String jsonResponse = gson.toJson(response);

        LOG.debug("Response message {} is \n{}", aRequest.getRequestURI(), jsonResponse);
        aResponse.setContentType("application/json");
        aResponse.getWriter().write(jsonResponse);
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
