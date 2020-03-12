package com.interview.ceg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class EventData {

    @JsonProperty("headers")
    private Map<String, Object> headers;
    @JsonProperty("payload")
    private Object payload;
}
