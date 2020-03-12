package com.interview.ceg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Event {

    @JsonProperty("metadata")
    private EventMetaData metadata;
    @JsonProperty("data")
    private EventData data;
}
