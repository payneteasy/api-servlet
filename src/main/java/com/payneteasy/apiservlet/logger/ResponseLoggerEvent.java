package com.payneteasy.apiservlet.logger;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Builder
public class ResponseLoggerEvent {
    String jsonBody;
    Object responseObject;
    int    httpResponseCode;
    long   timestamp;
}
