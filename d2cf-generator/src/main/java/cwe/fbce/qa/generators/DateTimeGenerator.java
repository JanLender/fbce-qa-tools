package cwe.fbce.qa.generators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DateTimeGenerator implements Iterator<LocalDateTime> {

    private LocalDateTime nextDateTime;
    private LocalDateTime endDateTimeExclusive;
    private final int incrementInMinutes;


    public DateTimeGenerator(Configuration configuration) {
        initStartAndNextDateTime(configuration);
        initEndDateTime(configuration);
        incrementInMinutes = configuration.getTimeIncrementInMinutes();
    }

    private void initEndDateTime(Configuration configuration) {
        LocalDate endDate = configuration.getScenarioDate();
        LocalTime endTime = configuration.getSummerStartTime();
        if (DaylightSavingTime.from(endDate).isWinterTime()) {
            endTime = endTime.plusHours(1);
        }

        endDateTimeExclusive = LocalDateTime.of(endDate, endTime);
    }

    private void initStartAndNextDateTime(Configuration configuration) {
        LocalDate startDate = configuration.getScenarioDate().minusDays(1);
        LocalTime startTime = configuration.getSummerStartTime();
        if (DaylightSavingTime.from(startDate).isWinterTime()) {
            startTime = startTime.plusHours(1);
        }
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        nextDateTime = startDateTime;
    }


    @Override
    public boolean hasNext() {
        return nextDateTime.isBefore(endDateTimeExclusive);
    }

    @Override
    public LocalDateTime next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There's no next datetime available.");
        }
        LocalDateTime result = nextDateTime;
        nextDateTime = nextDateTime.plusMinutes(incrementInMinutes);
        return result;
    }
}
