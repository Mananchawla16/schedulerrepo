package com.test.pavedroad.intcollabsnotification.core.constants;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ApplicationConstants {
    public static final String LOCAL_ENVIRONMENT = "LOCAL";

    public static final String RDS_COMBINED_CA_BUNDLE_PATH = "/rds-combined-ca-bundle.pem";

    public static final String SCHEDULER_LOG_IDENTIFIER = "SchedulerService";

    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss VV";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            ApplicationConstants.DATE_TIME_FORMAT);

    public static final String TIME_INTERVAL_REGEX = "\\d+\\s+(minute|hour|day|week|month|year)s?";

    public static final Pattern TIME_INTERVAL_PATTERN = Pattern.compile(TIME_INTERVAL_REGEX);

    public static final String TIME_INTERVALS_REGEX = "\\d+\\s+(minute|hour|day|week|month|year)" +
            "s?(\\s*,\\s*\\d+\\s+(minute|hour|day|week|month|year)s?)*";

    public static final Pattern TIME_INTERVALS_PATTERN = Pattern.compile(TIME_INTERVALS_REGEX);

    public static final String BLANK = " ";

    public static final String COMMA = ",";

    public static final String DEFAULT_TIME_ZONE = "America/Los_Angeles";

    public static final String MONGO_ID = "_id";

    public static final String JOB_ID = "jobId";

    public static final String DISPATCHER = "dispatcher";

    public static final String PRODUCER_CONFIG = "ProducerConfig";

    public static final String INTUIT_TID = "intuit_tid";
}
