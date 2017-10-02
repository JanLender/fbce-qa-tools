package cwe.fbce.qa.generators;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ScenarioTimeProcessor implements EntryProcessor {

    // <md:Model.scenarioTime>2016-09-05T10:30:00.000Z</md:Model.scenarioTime>

    public ScenarioTimeProcessor(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    private LocalDateTime dateTime;

    private static final String REGEXP = "(\\s*<md:Model\\.scenarioTime>\\s*).*?(\\s*</md:Model.scenarioTime>\\s*)";
    static final Pattern PATTERN = Pattern.compile(REGEXP);


    private static final String FORMAT_STRING = "<md:Model.scenarioTime>2016-09-05T10:30:00.000Z</md:Model.scenarioTime>";


    public boolean canProcess(String entry) {
        return PATTERN.matcher(entry).matches();
    }

    public String process(String entry) {
        String dateTimeFormatted = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return entry.replaceAll(REGEXP, String.format("$1%s.000Z$2", dateTimeFormatted));
    }
}
