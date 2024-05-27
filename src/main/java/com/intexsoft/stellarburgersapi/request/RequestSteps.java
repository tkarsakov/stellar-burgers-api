package com.intexsoft.stellarburgersapi.request;

import com.intexsoft.stellarburgersapi.model.ExistingUser;
import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.model.UserField;
import com.intexsoft.stellarburgersapi.service.EndpointService;
import com.intexsoft.stellarburgersapi.util.JSONUtil;
import io.restassured.response.Response;

import java.util.List;
import java.util.Optional;

import static com.intexsoft.stellarburgersapi.request.Method.*;

public class RequestSteps {
    private final RequestFactory requestFactory = new RequestFactory();

    public Response registerUser(NewUser newUser) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
        requestHeader.addParameters("Content-Type", "application/json");
        requestBody.addParameters("body", JSONUtil.convertToJSONString(newUser));
        return requestFactory.sendRequest(POST, EndpointService.AUTH_REGISTER, List.of(requestHeader, requestBody));
    }

    public Response logIn(ExistingUser existingUser) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
        requestHeader.addParameters("Content-Type", "application/json");
        requestBody.addParameters("body", JSONUtil.convertToJSONString(existingUser));
        return requestFactory.sendRequest(POST, EndpointService.AUTH_LOGIN, List.of(requestHeader, requestBody));
    }

    public Response changeUserData(UserField userField, String data, String accessToken, Boolean authenticate) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);

        if (authenticate) {
            requestHeader.addParameters("Content-Type", "application/json", "Authorization", accessToken);
        } else {
            requestHeader.addParameters("Content-Type", "application/json");
        }
        requestBody.addParameters("body",
                "{" +
                        "\"" + userField.name().toLowerCase() + "\":" + "\"" + data + "\""
                        + "}");
        return requestFactory.sendRequest(PATCH, EndpointService.AUTH_USER, List.of(requestHeader, requestBody));
    }

    public Optional<Response> deleteCreatedUser(String accessToken) {
        if (accessToken != null) {
            RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
            requestHeader.addParameters("Authorization", accessToken);
            return Optional.of(requestFactory.sendRequest(DELETE, EndpointService.AUTH_USER, List.of(requestHeader)));
        }
        return Optional.empty();
    }
}
