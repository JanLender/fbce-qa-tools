package cwe.fbce.qa.generators;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.util.Arrays;

public class Application {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Source & target directory expected as a single parameter. Given args: " + Arrays.toString(args));
            return;
        }
        File dir = new File(args[0]);
        if(!dir.exists()) {
            System.err.println("Given directory does not exist. " + dir.getAbsolutePath());
            return;
        }
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || (pathname.isFile()
                        && (pathname.getName().toLowerCase().endsWith(".xml")));
            }
        };
        DomainDocumentTransformer transformer = new DomainDocumentTransformer();
        transformer.setValidateOutput(true);
        transformFilesInDir(dir, filter, transformer);
    }

    private static void transformFilesInDir(File dir, FileFilter filter, DomainDocumentTransformer transformer) {
        for (File file : dir.listFiles(filter)) {
            if(file.isDirectory()){
                transformFilesInDir(file, filter, transformer);
            } else {
                System.gc();
                File sourceRenamed = new File(file.getAbsolutePath().replaceAll("\\.xml$", ".ucte.xml"));
                file.renameTo(sourceRenamed);
                File outputFile = file;
                file = sourceRenamed;
                System.out.format("Processing %s >> %s%n", file.getName(), outputFile.getName());
                try (OutputStream output = new FileOutputStream(outputFile); InputStream input = new FileInputStream(file)) {
                    transformer.transform(input, output);
                    if (transformer.isValidationPassed()) {
                        System.out.println("Output document is valid");
                    } else {
                        System.out.println("Output document is invalid:  " + transformer.getValidationMessage());
                    }
                    System.out.println();
                } catch (IOException | TransformerException | SAXException | XPathExpressionException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
                sourceRenamed.delete();
            }
        }
    }
}
