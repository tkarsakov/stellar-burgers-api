package com.intexsoft.stellarburgersapi.request;

import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RequestFactory {
    public Response sendRequest(Method method, String endpointURL, List<RequestParameter> requestParameterList) {
        RequestSpecification requestSpecification = createRequest(requestParameterList);
        return requestSpecification
                .request(method.name(), endpointURL).
                then()
                .extract()
                .response();
    }

    public RequestSpecification createRequest(List<RequestParameter> requestParameterList) {
        RequestSpecification requestSpecification = given();
        for (RequestParameter requestParameter : requestParameterList) {
            inputParameterIntoSpec(requestSpecification, requestParameter);
        }
        return requestSpecification;
    }

    private void inputParameterIntoSpec(RequestSpecification requestSpecification, RequestParameter requestParameter) {
        switch (requestParameter.getParameterType()) {
            case HEADER:
                for (Map.Entry<String, String> header : requestParameter.getParameters().entrySet()) {
                    requestSpecification.header(new Header(header.getKey(), header.getValue()));
                }
                break;
            case BODY:
                requestSpecification.body(requestParameter.getParameters().get("body"));
                break;
            default:
                throw new RuntimeException("Parameter type not implemented");
        }
    }
}
