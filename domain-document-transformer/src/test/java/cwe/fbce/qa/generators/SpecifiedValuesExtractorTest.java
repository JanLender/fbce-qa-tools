package cwe.fbce.qa.generators;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SpecifiedValuesExtractorTest {
    @Test
    public void getSpecifiedValues() throws Exception {

        List<String> items = new SpecifiedValuesExtractor().getSpecifiedValues(Tests.getInputDocument());
        assertNotNull(items);
        assertTrue(items.size() > 0);
        System.out.println(items);
    }

}