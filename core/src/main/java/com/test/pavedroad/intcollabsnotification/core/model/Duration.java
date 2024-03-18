package com.test.pavedroad.intcollabsnotification.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum Duration {
    MINUTE("minute"),
    MINUTES("minutes"),
    HOUR("hour"),
    HOURS("hours"),
    DAY("day"),
    DAYS("days"),
    WEEK("week"),
    WEEKS("weeks"),
    MONTH("month"),
    MONTHS("months"),
    YEAR("year"),
    YEARS("years");

    @Getter String value;
}
