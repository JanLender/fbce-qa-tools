package cwe.fbce.qa.generators;


import java.util.regex.Pattern;

public class DependentOnProcessor implements EntryProcessor {

    // <md:Model.DependentOn rdf:resource="urn:uuid:_028e5c32-079b-4b56-a248-6103d0228897" />

    private String refId;

    public DependentOnProcessor(String refId) {
        this.refId =refId;
    }

    private static final String REGEXP = "(\\s*<md:Model.DependentOn rdf:resource=\").*?(\"\\s*/>.*)";
    static final Pattern PATTERN = Pattern.compile(REGEXP);





    public boolean canProcess(String entry) {
        return PATTERN.matcher(entry).matches();
    }

    public String process(String entry) {

        return entry.replaceAll(REGEXP, String.format("$1urn:uuid:_%s$2", refId));

    }
}
