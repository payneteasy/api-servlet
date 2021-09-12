package com.payneteasy.apiservlet.problem;

/**
 * See https://tools.ietf.org/html/rfc7807
 */
public interface IProblemType {

    String getType();

    int getStatusCode();

    String getTitle();
}
