package com.test.pavedroad.intcollabsnotification.core.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import com.test.pavedroad.intcollabsnotification.core.model.DispatcherBeanConfig;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DispatcherFactory<T> {
    private final ApplicationContext applicationContext;
    @Getter
    private Map<String, DispatcherBeanConfig> dispatcherConfigMap = new HashMap<>();
    @Getter
    private Map<String, TriggerDispatcher> dispatcherBeanMap = new HashMap<>();
    private List<String> enabledDispatchers = new ArrayList<>();

    private void initializeDispatcherBeanMap() {
        String[] beanNames = applicationContext.getBeanNamesForType(TriggerDispatcher.class);
        enabledDispatchers.addAll(Arrays.asList(beanNames));
        for (String dispatcher : enabledDispatchers) {
            TriggerDispatcher<?, ?> triggerDispatcher = applicationContext.getBean(dispatcher, TriggerDispatcher.class);
            dispatcherBeanMap.put(dispatcher.toLowerCase(), triggerDispatcher);
        }
    }

    private void initializeDispatcherConfigMap() {
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @PostConstruct
    private void initialize() {
        initializeDispatcherBeanMap();
        initializeDispatcherConfigMap();
    }

    public void validateConfigName(String configName) throws Exception {
        if (StringUtils.isNotEmpty(configName) && !dispatcherConfigMap.containsKey(configName)) {
            throw new Exception("Dispatcher config not found");
        }
    }
}

