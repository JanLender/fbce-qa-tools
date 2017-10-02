package cwe.fbce.qa.generators;

import org.junit.Test;

import static org.junit.Assert.*;

public class AboutProcessorTest {

    private EntryProcessor processor = new AboutProcessor("XXXX-XXXX-XXXX");

    private String input = "    <md:FullModel rdf:about=\"urn:uuid:_e1ecce77-7561-4d7c-8054-a1b2bc8b98a4\">";

    @Test
    public void canProcess() throws Exception {
        assertTrue(processor.canProcess(input));
    }

    @Test
    public void process() throws Exception {
        String actual = processor.process(input);
        String expected = "    <md:FullModel rdf:about=\"urn:uuid:_XXXX-XXXX-XXXX\">";
        assertEquals(expected, actual);
    }

}