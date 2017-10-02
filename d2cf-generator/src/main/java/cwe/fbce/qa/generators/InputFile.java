package cwe.fbce.qa.generators;

import java.io.File;

public class InputFile {


    private File file;

    public FileType getType() {
        return type;
    }

    private FileType type;

    public InputFile(String inputFileName, FileType fileType) {
        this(new File(inputFileName), fileType);
    }

    public boolean isEQFile() {
        return type == FileType.EQ;
    }

    public File getFile() {
        return file;
    }

    public InputFile(File file, FileType fileType) {
        if (!file.exists() || ! file.canRead()) {
            throw  new IllegalArgumentException("Can't read a file " + file);
        }
        this.type = fileType;
        this.file = file;
    }




}
