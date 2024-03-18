package com.test.pavedroad.intcollabsnotification.core.producer;


import com.test.pavedroad.intcollabsnotification.core.model.Trigger;

public interface TriggerDispatcher<T extends Trigger, U> {
    /**
     * Publishes message to topic
     *
     * @param payload message to publish
     * @param config  producer config properties
     */
    void publishTrigger(T payload, U config);
}
