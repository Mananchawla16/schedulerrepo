package com.test.pavedroad.intcollabsnotification.core.util;

import com.test.pavedroad.intcollabsnotification.core.model.DispatcherBeanConfig;
import com.test.pavedroad.intcollabsnotification.core.model.Trigger;
import com.test.pavedroad.intcollabsnotification.core.producer.TriggerDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static io.vavr.API.*;
import com.test.pavedroad.intcollabsnotification.core.model.Duration;
import com.test.pavedroad.intcollabsnotification.core.constants.ApplicationConstants;
import com.test.pavedroad.intcollabsnotification.core.producer.DispatcherFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobHandlerUtils {
    private final DispatcherFactory dispatcherFactory;

    /**
     * Method to get ChronoUnit from time unit string
     *
     * @return
     */
    public ChronoUnit getChronoUnit(String timeUnit) {
        return Match(timeUnit.toLowerCase()).of(Case($(Duration.MINUTE.getValue()), ChronoUnit.MINUTES),
                Case($(Duration.MINUTES.getValue()), ChronoUnit.MINUTES),
                Case($(Duration.HOUR.getValue()), ChronoUnit.HOURS),
                Case($(Duration.HOURS.getValue()), ChronoUnit.HOURS),
                Case($(Duration.DAY.getValue()), ChronoUnit.DAYS),
                Case($(Duration.DAYS.getValue()), ChronoUnit.DAYS),
                Case($(Duration.WEEK.getValue()), ChronoUnit.WEEKS),
                Case($(Duration.WEEKS.getValue()), ChronoUnit.WEEKS),
                Case($(Duration.MONTH.getValue()), ChronoUnit.MONTHS),
                Case($(Duration.MONTHS.getValue()), ChronoUnit.MONTHS),
                Case($(Duration.YEAR.getValue()), ChronoUnit.YEARS),
                Case($(Duration.YEARS.getValue()), ChronoUnit.YEARS));
    }

    /**
     * Method to add interval to time and return ZonedDateTime
     *
     * @return
     */
    public ZonedDateTime getZonedDateTimeWithOffset(String time, String interval) {
        String[] repeatInterval = interval.trim().split(ApplicationConstants.BLANK);
        Long amountToAdd = Long.valueOf(repeatInterval[0]);
        String timeUnit = repeatInterval[1];
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, ApplicationConstants.DATE_TIME_FORMATTER);
        return zonedDateTime.plus(amountToAdd, getChronoUnit(timeUnit));
    }

    /**
     * Method to check if trigger has exceeded the grace period
     *
     * @return
     */
    public boolean checkValidityAsPerGracePeriod(
            String scheduleId, String triggerAt, String gracePeriod, ZonedDateTime zonedTimeNow) {
        if (StringUtils.isNotBlank(gracePeriod)) {
            ZonedDateTime triggerBefore = getZonedDateTimeWithOffset(triggerAt, gracePeriod);

            if (zonedTimeNow.isAfter(triggerBefore)) {
                log.info(
                        "{} | checkValidityAsPerGracePeriod | triggerSuccess=false | trigger with scheduleId={} was " +
                                "not executed as current time exceeds grace period of {}",
                        ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, scheduleId, gracePeriod);
                return false;
            }
        }
        return true;
    }

    public void pickBeanAndPushDataToDispatcher(Trigger triggerRequest) {
        DispatcherBeanConfig dispatcherBeanConfig = (DispatcherBeanConfig) dispatcherFactory.getDispatcherConfigMap()
                .get(triggerRequest.getDispatcherConfig());
        TriggerDispatcher dispatcher = (TriggerDispatcher) dispatcherFactory.getDispatcherBeanMap()
                .get(dispatcherBeanConfig.getBeanName());
        dispatcher.publishTrigger(triggerRequest, dispatcherBeanConfig.getConfig());
    }
}
