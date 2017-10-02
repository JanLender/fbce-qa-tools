package cwe.fbce.qa.generators;


import java.util.regex.Pattern;

    public class AboutProcessor implements EntryProcessor {

        // <md:FullModel rdf:about="urn:uuid:_e1ecce77-7561-4d7c-8054-a1b2bc8b98a4">

        private String id;

        public AboutProcessor(String id) {
            this.id = id;
        }


        private static final String REGEXP = "(\\s*<md:FullModel rdf:about=\").*?(\".*)";
        static final Pattern PATTERN = Pattern.compile(REGEXP);





        public boolean canProcess(String entry) {
            return PATTERN.matcher(entry).matches();
        }

        public String process(String entry) {
            return entry.replaceAll(REGEXP, String.format("$1urn:uuid:_%s$2", id));
        }
    }
