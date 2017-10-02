package cwe.fbce.qa.generators;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

import static org.junit.Assert.*;

public class ConfigurationTest {

    @Test
    public void loadConfig() {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.load(properties);
        LocalDate expected = LocalDate.of(2016, 11, 10);
        assertEquals(expected, configuration.getScenarioDate());
        assertNotNull(configuration.getTsoCodes());
        System.out.println(configuration.getTsoCodes());
    }

}