package com.payneteasy.apiservlet;

public interface IApiCall<Request, Response> {

    Response exec(Request aRequest) throws Exception;

}
