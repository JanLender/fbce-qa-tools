package cwe.fbce.qa.generators;


import java.util.regex.Pattern;

public class VersionProcessor implements EntryProcessor {

    // <md:Model.version>1</md:Model.version>

    private int version;

    public VersionProcessor(int version) {
        this.version =version;
    }


    private static final String REGEXP = "(\\s*<md:Model.version>)1(</md:Model.version>.*)";
    static final Pattern PATTERN = Pattern.compile(REGEXP);





    public boolean canProcess(String entry) {
        return PATTERN.matcher(entry).matches();
    }

    public String process(String entry) {
        return entry.replaceAll(REGEXP, String.format("$1%d$2", version));
    }
}
