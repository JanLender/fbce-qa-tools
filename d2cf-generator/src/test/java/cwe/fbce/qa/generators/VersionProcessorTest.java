package cwe.fbce.qa.generators;

import org.junit.Test;

import static org.junit.Assert.*;

public class VersionProcessorTest {

    EntryProcessor processor = new VersionProcessor(123);

    String input = "   <md:Model.version>1</md:Model.version>    ";

    @Test
    public void canProcess() throws Exception {
        assertTrue(processor.canProcess(input));
    }

    @Test
    public void process() throws Exception {
        String actual = processor.process(input);
        String expected = "   <md:Model.version>123</md:Model.version>    ";
        assertEquals(expected, actual);
    }

}