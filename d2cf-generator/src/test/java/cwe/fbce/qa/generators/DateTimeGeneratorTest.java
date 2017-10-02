package cwe.fbce.qa.generators;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class DateTimeGeneratorTest {


    private Configuration configuration() {
        Configuration configuration = new Configuration();
        configuration.setScenarioDate(LocalDate.of(2016, 11, 21));
        configuration.setTimeIncrementInMinutes(60);
        configuration.setSummerStartTime(LocalTime.of(21,30));
        return configuration;
    }

    @Test
    public void TestInit() {
        Configuration configuration =configuration();
        DateTimeGenerator generator = new DateTimeGenerator(configuration);
        assertTrue(generator.hasNext());
        System.out.println(generator.next());
    }

    @Test
    public void correctLastValue() {
        Configuration configuration =configuration();
        DateTimeGenerator generator = new DateTimeGenerator(configuration);
        LocalDateTime last = null;
        while (generator.hasNext()) {
            last = generator.next();
        }
        assertEquals("2016-11-21T21:30", last.toString());

    }
    @Test
    public void correctFirstValue() {
        Configuration configuration =configuration();
        DateTimeGenerator generator = new DateTimeGenerator(configuration);
        LocalDateTime first = generator.next();

        assertEquals("2016-11-20T22:30", first.toString());

    }

}