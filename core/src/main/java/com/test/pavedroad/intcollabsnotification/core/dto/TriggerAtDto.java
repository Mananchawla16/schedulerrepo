package com.test.pavedroad.intcollabsnotification.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.test.pavedroad.intcollabsnotification.core.constants.ApplicationConstants;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@JsonDeserialize(builder = TriggerAtDto.TriggerAtDtoBuilder.class)
public class TriggerAtDto implements Serializable {

    private final String time;

    private final String zone;

    @JsonCreator
    public TriggerAtDto(String time, String zone) {
        this.time = time;
        this.zone = (zone != null) ? zone : ApplicationConstants.DEFAULT_TIME_ZONE;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TriggerAtDtoBuilder {}
}
