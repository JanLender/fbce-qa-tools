package cwe.fbce.qa.generators;

class DefaultEntryProcessor implements EntryProcessor {
    @Override
    public boolean canProcess(String entry) {
        return true;
    }

    @Override
    public String process(String entry) {
        return entry;
    }
}
