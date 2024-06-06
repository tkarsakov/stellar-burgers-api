package com.intexsoft.stellarburgersapi.request;

import com.intexsoft.stellarburgersapi.model.ExistingUser;
import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.model.Order;
import com.intexsoft.stellarburgersapi.model.UserField;
import com.intexsoft.stellarburgersapi.service.EndpointService;
import com.intexsoft.stellarburgersapi.util.JSONUtil;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.intexsoft.stellarburgersapi.request.Method.*;

public class Steps {
    private final RequestFactory requestFactory = new RequestFactory();

    @Step("Регистрация пользователя {newUser}")
    public Response registerUser(NewUser newUser) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
        requestHeader.addParameters("Content-Type", "application/json");
        requestBody.addParameters(JSONUtil.convertToJSONString(newUser));
        return requestFactory.sendRequest(POST, EndpointService.AUTH_REGISTER, List.of(requestHeader, requestBody));
    }

    @Step("Попытка лоигна пользователем с данными {existingUser}")
    public Response logIn(ExistingUser existingUser) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
        requestHeader.addParameters("Content-Type", "application/json");
        requestBody.addParameters(JSONUtil.convertToJSONString(existingUser));
        return requestFactory.sendRequest(POST, EndpointService.AUTH_LOGIN, List.of(requestHeader, requestBody));
    }

    @Step("Попытка изменения поля {userField} пользователем с токеном {accessToken}")
    public Response changeUserData(UserField userField, String data, String accessToken) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
        requestHeader.addParameters("Content-Type", "application/json", "Authorization", accessToken);
        requestBody.addParameters("{" +
                "\"" + userField.name().toLowerCase() + "\":" + "\"" + data + "\""
                + "}");
        return requestFactory.sendRequest(PATCH, EndpointService.AUTH_USER, List.of(requestHeader, requestBody));
    }

    @Step("Попытка изменения поля {userField} без токена")
    public Response changeUserData(UserField userField, String data) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);

        requestBody.addParameters("{" +
                "\"" + userField.name().toLowerCase() + "\":" + "\"" + data + "\""
                + "}");
        return requestFactory.sendRequest(PATCH, EndpointService.AUTH_USER, List.of(requestHeader, requestBody));
    }

    @Step("Удаление текущего пльзователя с токеном {accessToken}")
    public Optional<Response> deleteCreatedUser(String accessToken) {
        if (accessToken != null) {
            RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
            requestHeader.addParameters("Authorization", accessToken);
            return Optional.of(requestFactory.sendRequest(DELETE, EndpointService.AUTH_USER, List.of(requestHeader)));
        }
        return Optional.empty();
    }

    @Step("Создание заказа {order} с токеном {accessToken}")
    public Response createOrder(String accessToken, Order order) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
        requestHeader.addParameters("Content-Type", "application/json", "Authorization", accessToken);
        requestBody.addParameters(JSONUtil.convertToJSONString(order));
        return requestFactory.sendRequest(POST, EndpointService.ORDERS, List.of(requestHeader, requestBody));
    }

    @Step("Создание заказа {order} без токена")
    public Response createOrder(Order order) {
        RequestParameter requestBody = new RequestParameter(ParameterType.BODY);
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
        requestHeader.addParameters("Content-Type", "application/json");
        requestBody.addParameters(JSONUtil.convertToJSONString(order));
        return requestFactory.sendRequest(POST, EndpointService.ORDERS, List.of(requestHeader, requestBody));
    }

    @Step("Получение заказов пользователя с токеном {accessToken}")
    public Response getUsersOrders(String accessToken) {
        RequestParameter requestHeader = new RequestParameter(ParameterType.HEADER);
        requestHeader.addParameters("Content-Type", "application/json", "Authorization", accessToken);
        return requestFactory.sendRequest(GET, EndpointService.ORDERS, List.of(requestHeader));
    }

    @Step("Получение заказов без токена")
    public Response getOrders() {
        return requestFactory.sendRequest(GET, EndpointService.ORDERS, new ArrayList<>());
    }
}
