package cwe.fbce.qa.generators;

import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class GeneratorTest {


    private Configuration configuration() {
        Configuration configuration = new Configuration();
        File outputFile = new File("output/bulk.test.zip");
        File parent = outputFile.getParentFile();
        parent.mkdirs();
        outputFile.delete();
        configuration.setOutputFile(outputFile.getPath());

        //2016-10-29 22:00:00
        configuration.setScenarioDate(LocalDate.of(2016, 10, 30));
        configuration.setSummerStartTime(LocalTime.of(22,30));

        configuration.setTsoCodes(new ArrayList<>(Arrays.asList("TTN","D4","D2","BE","AT","FR","D7")));
        configuration.setSshInputFileName("src/test/resources/20160905T1030Z_2D_TTN_SSH_001.xml");
        configuration.setEqInputFileName("src/test/resources/20160905T1030Z_TTN_EQ_001.xml");
        configuration.setTpInputFileName("src/test/resources/20160905T1030Z_2D_TTN_TP_001.xml");
        configuration.setSvInputFileName("src/test/resources/20160905T1030Z_2D_CGMCE_SV_001.xml");
        configuration.setEqBdInputFileName("src/test/resources/20150211T0030Z_2D_EQ_BD_005.xml");
        configuration.setTpBdInputFileName("src/test/resources/20150211T0030Z_2D_TP_BD_005.xml");
        return configuration;
    }

    @Test
    public void generate() throws Exception {
        Configuration configuration = configuration();
        System.out.println(new File(configuration.getOutputFile()).getAbsolutePath());
        Generator generator = new Generator(configuration);
        generator.generate();

    }

}