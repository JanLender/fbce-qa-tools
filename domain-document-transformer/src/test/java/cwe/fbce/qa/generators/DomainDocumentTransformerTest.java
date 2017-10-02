package cwe.fbce.qa.generators;

import org.junit.Test;

import static org.junit.Assert.*;

public class DomainDocumentTransformerTest {

    @Test
    public void transform() throws Exception {
        DomainDocumentTransformer transformer = new DomainDocumentTransformer();
        transformer.setValidateOutput(true);
        transformer.transform(this.getClass().getResourceAsStream("/fb_final_enlarged.xml"), System.out);
        System.out.println();
        System.out.println("Validation message: " + transformer.getValidationMessage());
        assertTrue(transformer.getValidationMessage(),transformer.isValidationPassed());
    }

}