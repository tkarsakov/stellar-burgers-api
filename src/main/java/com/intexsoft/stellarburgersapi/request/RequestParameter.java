package com.intexsoft.stellarburgersapi.request;

import java.util.ArrayList;
import java.util.List;

public class RequestParameter {
    private final ParameterType parameterType;
    private final List<String> parameters = new ArrayList<>();

    public RequestParameter(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public void addParameters(String... kv) {
        if (kv.length == 1) {
            parameters.add(kv[0]);
        } else {
            for (int i = 0; i < kv.length - 1; i += 2) {
                parameters.add(kv[i]);
                parameters.add(kv[i + 1]);
            }
        }
    }

    public List<String> getParameters() {
        return parameters;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }
}
