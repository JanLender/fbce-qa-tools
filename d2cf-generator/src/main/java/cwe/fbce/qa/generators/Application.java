package cwe.fbce.qa.generators;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Application {

    public static void main(String[] args) {


        Configuration configuration = new Configuration();
        configuration.load(loadProperties());
        new Generator(configuration).generate();

    }
    // /Users/jan/workspace/fbce-support/tp-eq-ssh-whole-day-data-generator/config.properties

     static Properties loadProperties() {

        Properties properties = new Properties();



        try(InputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);

        } catch (Exception e) {
            System.out.println("Couldn't load property file");
            System.exit(1);
        }


        return properties;
    }
}
