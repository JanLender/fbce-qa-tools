package cwe.fbce.qa.generators;

import org.junit.Test;

import static org.junit.Assert.*;

public class DependentOnProcessorTest {

    String input= "    <md:Model.DependentOn rdf:resource=\"urn:uuid:_028e5c32-079b-4b56-a248-6103d0228897\" />    ";
    String inputNoSpaceAfterAttribute
            = "    <md:Model.DependentOn rdf:resource=\"urn:uuid:_028e5c32-079b-4b56-a248-6103d0228897\"/>    ";
    private EntryProcessor processor = new DependentOnProcessor("XXXX-XXXX-XXXX");


    @Test
    public void canProcess() throws Exception {
        assertTrue(processor.canProcess(input));
    }
    @Test
    public void canProcessNoSpaceAfterAttribute() throws Exception {
        assertTrue(processor.canProcess(inputNoSpaceAfterAttribute));
    }

    @Test
    public void process() throws Exception {
        String actual = processor.process(input);
        String expected = "    <md:Model.DependentOn rdf:resource=\"urn:uuid:_XXXX-XXXX-XXXX\" />    ";
        assertEquals(expected, actual);
    }
    @Test
    public void processNoSpaceAfterAttribute() throws Exception {
        String actual = processor.process(inputNoSpaceAfterAttribute);
        String expected = "    <md:Model.DependentOn rdf:resource=\"urn:uuid:_XXXX-XXXX-XXXX\"/>    ";
        assertEquals(expected, actual);
    }

}