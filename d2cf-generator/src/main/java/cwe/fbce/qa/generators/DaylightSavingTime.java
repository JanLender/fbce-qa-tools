package cwe.fbce.qa.generators;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DaylightSavingTime {

    private LocalDate date;

    public static DaylightSavingTime from(LocalDate date) {
        DaylightSavingTime daylightSavingTime = new DaylightSavingTime();
        daylightSavingTime.date = date;
        return daylightSavingTime;
    }
    public static DaylightSavingTime from(LocalDateTime dateTime) {
        DaylightSavingTime daylightSavingTime = new DaylightSavingTime();
        daylightSavingTime.date = dateTime.toLocalDate();
        return daylightSavingTime;
    }

    static LocalDate getAutumnTimeChangeDay(int year) {
        LocalDate localDate = LocalDate.of(year, 10, 31);
        return getLastSunday(localDate);
    }

    static LocalDate getSpringDateChangeDay(int year) {

        LocalDate localDate = LocalDate.of(year, 3, 31);
        return getLastSunday(localDate);
    }

    private static LocalDate getLastSunday(LocalDate localDate) {

        return localDate.getDayOfWeek() == DayOfWeek.SUNDAY
                    ? localDate
                    : localDate.minusDays(localDate.getDayOfWeek().getValue());
    }

    public boolean isWinterTime() {
        return date.isBefore(getSpringDateChangeDay(date.getYear()))
                ||  date.isAfter(getAutumnTimeChangeDay(date.getYear()))
                ||  date.isEqual(getAutumnTimeChangeDay(date.getYear()));
    }

    public boolean isSummerTime() {
        return date.isBefore(getAutumnTimeChangeDay(date.getYear()))
                ||  date.isAfter(getSpringDateChangeDay(date.getYear()))
                ||  date.isEqual(getSpringDateChangeDay(date.getYear()));
    }

    public boolean isSpringTimeChangeDay() {
        return date.isEqual(getSpringDateChangeDay(date.getYear()));
    }

    public boolean isAutumnTimeChangeDay() {
        return date.isEqual(getAutumnTimeChangeDay(date.getYear()));
    }

    public int getNumberOfHours() {
        if (isSpringTimeChangeDay()) {
            return 23;
        }
        if (isAutumnTimeChangeDay()) {
            return 25;
        }
        return 24;
    }





}
