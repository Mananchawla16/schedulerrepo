package com.test.pavedroad.intcollabsnotification.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DispatcherBeanConfig<T> {
    private String beanName;
    private T config;
}
