package com.intexsoft.stellarburgersapi.request;

import java.util.HashMap;
import java.util.Map;

public class RequestParameter {
    private final ParameterType parameterType;
    private final Map<String, String> parameters = new HashMap<>();

    public RequestParameter(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public void addParameters(String... kv) {
        for (int i = 0; i < kv.length - 1; i += 2) {
            parameters.put(kv[i], kv[i + 1]);
        }
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }
}
