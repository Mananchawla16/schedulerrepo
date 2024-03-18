package com.test.pavedroad.intcollabsnotification.schedulerapp;

import com.test.pavedroad.intcollabsnotification.core.JobRunrStorageConfiguration;

import com.test.pavedroad.intcollabsnotification.core.MongoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({JobRunrStorageConfiguration.class, MongoProperties.class})
public class SchedulerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerWebApplication.class, args);
    }
}
