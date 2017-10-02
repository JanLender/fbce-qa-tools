package cwe.fbce.qa.generators;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class ScenarioTimeProcessorTest {

    @Test
    public void process() {
        String input = "    <md:Model.scenarioTime>2015-09-05T10:30:00.000Z</md:Model.scenarioTime>    ";
        String expected = "    <md:Model.scenarioTime>2016-09-01T13:11:00.000Z</md:Model.scenarioTime>    ";
        LocalDateTime dateTime = LocalDateTime.of(2016,9,1,13,11);
        ScenarioTimeProcessor processor = new ScenarioTimeProcessor(dateTime);
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

}