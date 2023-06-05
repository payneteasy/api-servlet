package com.payneteasy.apiservlet;

import com.google.gson.Gson;
import com.payneteasy.apiservlet.logger.ILoggerContext;
import com.payneteasy.apiservlet.logger.ILoggerContextFactory;
import com.payneteasy.apiservlet.logger.RequestLoggerEvent;
import com.payneteasy.apiservlet.logger.ResponseLoggerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.payneteasy.apiservlet.logger.noop.LoggerContextFactoryNoop.NOOP_CONTEXT_FACTORY;

public class GsonApiServlet<I, O> extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GsonApiServlet.class);

    private final IApiCall<I, O>        apiCall;
    private final Class<I>              requestClass;
    private final Gson                  gson;
    private final boolean               isVoidRequest;
    private final IExceptionHandler     exceptionHandler;
    private final IRequestValidator     requestValidator;
    private final ILoggerContextFactory loggerContextFactory;

    public GsonApiServlet(IApiCall<I, O> aApiCall, Class<I> aRequestClass, Gson aGson, IExceptionHandler aExceptionHandler, IRequestValidator aRequestValidator) {
        this(aApiCall, aRequestClass, aGson, aExceptionHandler, aRequestValidator, NOOP_CONTEXT_FACTORY);
    }

    public GsonApiServlet(
            IApiCall<I, O> aApiCall
            , Class<I> aRequestClass
            , Gson aGson
            , IExceptionHandler aExceptionHandler
            , IRequestValidator aRequestValidator
            , ILoggerContextFactory aLoggerContextFactory
    ) {
        apiCall = aApiCall;
        gson = aGson;
        requestClass = aRequestClass;
        isVoidRequest = VoidRequest.class.equals(aRequestClass);
        exceptionHandler = aExceptionHandler;
        requestValidator = aRequestValidator;
        loggerContextFactory = aLoggerContextFactory;
    }

    @Override
    protected void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        ILoggerContext loggerContext = loggerContextFactory.createLoggerContext();
        try {
            String json = toJsonString(aRequest.getReader());
            LOG.debug("Incoming json to {} is {}", aRequest.getRequestURI(), json);

            //noinspection unchecked
            I request = isVoidRequest ? (I) VoidRequest.VOID_REQUEST : gson.fromJson(json, requestClass);

            loggerContext.onRequest(RequestLoggerEvent.builder()
                    .httpServletRequest(aRequest)
                    .jsonBody(json)
                    .requestClass(requestClass)
                    .requestObject(request)
                    .timestamp(System.currentTimeMillis())
                    .build()
            );

            LOG.debug("Incoming POST message {} \n{}", aRequest.getRequestURI(), json);

            requestValidator.validateRequest(request, requestClass);

            processRequest(aRequest, aResponse, request, loggerContext);

        } catch (Exception e) {
            exceptionHandler.handleException(e, new ExceptionContextImpl(aResponse, loggerContext));
        }
    }

    @Override
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        ILoggerContext loggerContext = loggerContextFactory.createLoggerContext();
        try {
            loggerContext.onRequest(RequestLoggerEvent.builder()
                    .httpServletRequest(aRequest)
                    .timestamp(System.currentTimeMillis())
                    .build()
            );

            LOG.debug("Incoming GET message {}", aRequest.getRequestURI());

            processRequest(aRequest, aResponse, null, loggerContext);

        } catch (Exception e) {
            exceptionHandler.handleException(e, new ExceptionContextImpl(aResponse, loggerContext));
        }
    }

    private void processRequest(HttpServletRequest aRequest, HttpServletResponse aResponse, I request, ILoggerContext aLoggerContext) throws Exception {
        O      response     = apiCall.exec(request);
        String jsonResponse = gson.toJson(response);

        LOG.debug("Response message {} is \n{}", aRequest.getRequestURI(), jsonResponse);
        aResponse.setContentType("application/json");
        aResponse.getWriter().write(jsonResponse);

        aLoggerContext.onSuccessResponse(ResponseLoggerEvent.builder()
                .jsonBody(jsonResponse)
                .responseObject(response)
                .timestamp(System.currentTimeMillis())
                .build());
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
