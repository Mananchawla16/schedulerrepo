package com.test.pavedroad.intcollabsnotification.core.config;


import com.test.pavedroad.intcollabsnotification.core.dto.ScheduleTriggerDto;
import com.test.pavedroad.intcollabsnotification.core.model.ScheduleTriggerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper()
public interface TriggerMapper {
    @Mapping(target = "triggerAt", expression =
            "java(dto.getTriggerAt().getTime() + \" \" + dto.getTriggerAt().getZone())")
    ScheduleTriggerRequest mapScheduleTriggerDtoToEntity(ScheduleTriggerDto dto);


}