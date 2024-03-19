package com.test.pavedroad.intcollabsnotification.schedulerapp;

import com.test.pavedroad.intcollabsnotification.core.JobRunrStorageConfiguration;

import com.test.pavedroad.intcollabsnotification.core.MongoProperties;
import com.test.pavedroad.intcollabsnotification.core.config.LocalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@Import({JobRunrStorageConfiguration.class, MongoProperties.class, LocalConfig.class})
public class SchedulerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerWebApplication.class, args);
    }
}
