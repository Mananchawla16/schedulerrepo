package com.test.pavedroad.intcollabsnotification.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonDeserialize(builder = ScheduleTriggerDto.ScheduleTriggerDtoBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleTriggerDto extends TriggerDto implements Serializable {
    @Builder
    public ScheduleTriggerDto(
            String scheduleId, TriggerAtDto triggerAt, String gracePeriod, String triggerData,
            String dispatcherConfig) {
        super(scheduleId, triggerAt, gracePeriod, triggerData, dispatcherConfig);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ScheduleTriggerDtoBuilder extends TriggerDtoBuilder {}
}
