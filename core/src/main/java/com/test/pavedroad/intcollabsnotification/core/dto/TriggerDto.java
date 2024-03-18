package com.test.pavedroad.intcollabsnotification.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Builder
@JsonDeserialize(builder = TriggerDto.TriggerDtoBuilder.class)
public class TriggerDto {
    private final String scheduleId;

    @Valid
    private final TriggerAtDto triggerAt;

    private final String gracePeriod;

    @NotBlank(message = "Trigger data is mandatory")
    private final String triggerData;

    @NotBlank(message = "Dispatcher config name is mandatory")
    private final String dispatcherConfig;

    public TriggerDto(
            String scheduleId, TriggerAtDto triggerAt, String gracePeriod, String triggerData,
            String dispatcherConfig) {
        this.scheduleId = (scheduleId != null) ? scheduleId : UUID.randomUUID().toString();
        this.triggerAt = triggerAt;
        this.gracePeriod = gracePeriod;
        this.triggerData = triggerData;
        this.dispatcherConfig = dispatcherConfig;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TriggerDtoBuilder {}
}
