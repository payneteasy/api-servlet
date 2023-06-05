package com.payneteasy.apiservlet.logger;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.servlet.http.HttpServletRequest;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Builder
public class RequestLoggerEvent {
    HttpServletRequest httpServletRequest;
    String             jsonBody;
    Class<?>           requestClass;
    Object             requestObject;
    long               timestamp;
}
