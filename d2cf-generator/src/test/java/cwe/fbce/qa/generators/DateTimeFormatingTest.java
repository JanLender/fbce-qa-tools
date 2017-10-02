package cwe.fbce.qa.generators;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

public class DateTimeFormatingTest {

    @Test
    public void formatDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        // @formatter:off
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR, 4)
                .appendValue(ChronoField.MONTH_OF_YEAR,2)
                .appendValue(ChronoField.DAY_OF_MONTH,2)
                .appendLiteral('T')
                .appendValue(ChronoField.HOUR_OF_DAY,2)
                .appendValue(ChronoField.MINUTE_OF_HOUR,2)
                .appendLiteral('Z')
                .toFormatter();
        // @formatter:on
        System.out.println(dateTime.format(formatter));
    }
}
