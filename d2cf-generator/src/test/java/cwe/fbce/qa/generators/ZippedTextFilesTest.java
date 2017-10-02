package cwe.fbce.qa.generators;

import org.junit.Test;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZippedTextFilesTest {

    @Test
    public void zipTest() {
       try ( ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream("zippedTextFiles.zip")) ) {
           ZipEntry zipEntryA = new ZipEntry("first.txt");
           outputStream.putNextEntry(zipEntryA);
           PrintWriter writerA = new PrintWriter(outputStream);
            writerA.println("Hello. One.");
           writerA.flush();
           writerA.close();
           outputStream.closeEntry();

           outputStream.putNextEntry(new ZipEntry("second.txt"));
           PrintWriter writerB = new PrintWriter(outputStream);
           writerB.println("Hello. Two.");
           writerB.close();

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
}
