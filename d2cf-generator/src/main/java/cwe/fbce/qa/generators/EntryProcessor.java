package cwe.fbce.qa.generators;

public interface EntryProcessor {

    boolean canProcess(String entry);

    String process(String entry);
}
