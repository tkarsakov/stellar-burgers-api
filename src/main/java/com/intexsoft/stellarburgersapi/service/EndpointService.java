package com.intexsoft.stellarburgersapi.service;

public class EndpointService {
    private static final String URL = PropertiesService.getProperty(PropertiesFile.CONFIG, "url");
    public static final String AUTH_REGISTER = EndpointService.getFullEndpoint("/auth/register/");
    public static final String AUTH_USER = EndpointService.getFullEndpoint("/auth/user/");
    public static final String AUTH_LOGIN = EndpointService.getFullEndpoint("/auth/login/");
    public static final String ORDERS = EndpointService.getFullEndpoint("/orders/");

    public static String getFullEndpoint(String endpoint) {
        return URL + endpoint;
    }
}
