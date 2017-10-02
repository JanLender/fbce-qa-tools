package cwe.fbce.qa.generators;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class DaylightSavingTimeTest {

    @Test
    public void getAutumnDay() throws Exception {
        LocalDate localDate = DaylightSavingTime.getAutumnTimeChangeDay(2016);
        assertEquals(LocalDate.of(2016,10, 30), localDate);
    }

    @Test
    public void autumnDay25Hours() throws Exception {
        LocalDate date = LocalDate.of(2016,10, 30);
        assertEquals(25,DaylightSavingTime.from(date).getNumberOfHours());
    }

    @Test
    public void springDay23Hours() throws Exception {
        LocalDate date = LocalDate.of(2016,3, 27);
        assertEquals(23,DaylightSavingTime.from(date).getNumberOfHours());
    }

    @Test
    public void getSpringDay() throws Exception {
        LocalDate localDate = DaylightSavingTime.getSpringDateChangeDay(2016);
        assertEquals(LocalDate.of(2016,3, 27), localDate);
    }

    @Test
    public void springDayIsSummerTime() throws Exception {
        LocalDate date = LocalDate.of(2016,3, 27);
        assertTrue(DaylightSavingTime.from(date).isSummerTime());
    }

    @Test
    public void autumnDayIsWinterTime() throws Exception {
        LocalDate date = LocalDate.of(2016,10, 30);
        assertTrue(DaylightSavingTime.from(date).isWinterTime());
    }

    @Test
    public void winterTime() {
        assertTrue(DaylightSavingTime.from(LocalDate.of(2016,1,21)).isWinterTime());
    }

    @Test
    public void winterTimeAutumn() {
        assertTrue(DaylightSavingTime.from(LocalDate.of(2016,12,24)).isWinterTime());
    }


    @Test
    public void summerTime() {
        assertTrue(DaylightSavingTime.from(LocalDate.of(2016,9,13)).isSummerTime());
    }

    @Test
    public void winterTime24hours() {
        LocalDate date = LocalDate.of(2016, 1, 21);
        assertEquals(24,DaylightSavingTime.from(date).getNumberOfHours());
    }

    @Test
    public void winterTimeAutumn24hours() {
        LocalDate date = LocalDate.of(2016, 12, 24);
        assertEquals(24,DaylightSavingTime.from(date).getNumberOfHours());
    }


    @Test
    public void summerTime24hours() {

        LocalDate date = LocalDate.of(2016,9,13);
        assertEquals(24,DaylightSavingTime.from(date).getNumberOfHours());
    }

}