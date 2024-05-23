package com.intexsoft.stellarburgersapi.service;

public class EndpointService {
    private static final String URL = PropertiesService.getProperty(PropertiesFile.CONFIG, "url");

    public static String getFullEndpoint(String endpoint) {
        return URL + endpoint;
    }
}
