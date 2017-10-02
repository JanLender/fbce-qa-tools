package cwe.fbce.qa.generators;

import java.time.LocalDate;
import java.time.LocalDateTime;

public enum FileType {
    EQ ("%s_EQ"),SSH("2D_%s_SSH"),TP("2D_%s_TP");

    public String getFileNameStub() {
        return fileNameStub;
    }

    private final String fileNameStub;

    private FileType(String fileNameStub) {
        this.fileNameStub= fileNameStub;
    }


}
