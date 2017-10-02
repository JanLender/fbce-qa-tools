package cwe.fbce.qa.generators;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Configuration {

    private LocalDate scenarioDate;

private String eqInputFileName ;
private String sshInputFileName ;
    private int version = 1;
    private static String outputFile;
    private String svInputFileName;


    public LocalTime getSummerStartTime() {
        return summerStartTime;
    }

    public void setSummerStartTime(LocalTime summerStartTime) {
        this.summerStartTime = summerStartTime;
    }

    private LocalTime summerStartTime;

    public static String getOutputFile() {
        return outputFile == null ? "": outputFile;
    }

    public static void setOutputFile(String outputFile) {
        Configuration.outputFile = outputFile;
    }

    public String getEqInputFileName() {
        return eqInputFileName;
    }

    public void setEqInputFileName(String eqInputFileName) {
        this.eqInputFileName = eqInputFileName;
    }

    public String getSshInputFileName() {
        return sshInputFileName;
    }

    public void setSshInputFileName(String sshInputFileName) {
        this.sshInputFileName = sshInputFileName;
    }

    public String getTpInputFileName() {
        return tpInputFileName;
    }

    public void setTpInputFileName(String tpInputFileName) {
        this.tpInputFileName = tpInputFileName;
    }

    private String eqBdInputFileName;
    private String tpBdInputFileName;

    public String getEqBdInputFileName() {
        return eqBdInputFileName;
    }

    public void setEqBdInputFileName(String eqBdInputFileName) {
        this.eqBdInputFileName = eqBdInputFileName;
    }

    public String getTpBdInputFileName() {
        return tpBdInputFileName;
    }

    public void setTpBdInputFileName(String tpBdInputFileName) {
        this.tpBdInputFileName = tpBdInputFileName;
    }

    private List<String> tsoCodes;

    private String tpInputFileName ;

    public void setScenarioDate(LocalDate scenarioDate) {
        this.scenarioDate = scenarioDate;
    }

    public void setTimeIncrementInMinutes(int timeIncrementInMinutes) {
        this.timeIncrementInMinutes = timeIncrementInMinutes;
    }

    private int timeIncrementInMinutes = 60;

    public LocalDate getScenarioDate() {
        return scenarioDate;
    }

    public int getTimeIncrementInMinutes() {
        return timeIncrementInMinutes;
    }

    public DateTimeFormatter getDateTimeFormatterForFileNaming() {
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
        return formatter;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void load(Properties properties) {
        String scenarioDate = properties.getProperty("scenarioDate");
        this.scenarioDate = LocalDate.parse(scenarioDate, DateTimeFormatter.ISO_LOCAL_DATE);
        this.version = Integer.parseInt(properties.getProperty("version"));
        this.outputFile = properties.getProperty("outputFile", "./bulk.zip");
        this.eqInputFileName = properties.getProperty("eqInputFileName");
        this.tpInputFileName = properties.getProperty("tpInputFileName");
        this.sshInputFileName = properties.getProperty("sshInputFileName");
        this.svInputFileName = properties.getProperty("svInputFileName");
        this.eqBdInputFileName = properties.getProperty("eqBdInputFileName");
        this.tpBdInputFileName = properties.getProperty("tpBdInputFileName");
        this.summerStartTime = LocalTime.parse(properties.getProperty("summerStartTime"), getHourMinuteFormatter());
        this.timeIncrementInMinutes = Integer.parseInt(properties.getProperty("timeIncrementInMinutes", "60"));
        this.tsoCodes = new ArrayList<>(Arrays.asList(properties.getProperty("tsoCodes").split(",\\s*")));
    }

    private DateTimeFormatter getHourMinuteFormatter() {
        // @formatter:off
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.HOUR_OF_DAY,2)
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR,2)
                .toFormatter();
        // @formatter:on
        return formatter;
    }

    public List<String> getTsoCodes() {
        return tsoCodes;
    }

    public void setTsoCodes(List<String> tsoCodes) {
        this.tsoCodes = tsoCodes;
    }

    public void setSvInputFileName(String svInputFileName) {
        this.svInputFileName = svInputFileName;
    }

    public String getSvInputFileName() {
        return svInputFileName;
    }
}
