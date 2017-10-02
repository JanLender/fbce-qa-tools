package cwe.fbce.qa.generators;

import org.apache.log4j.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Generator {

    private static final Logger LOGGER = Logger.getLogger(Generator.class);
    private Collection<InputFile> inputFiles;
    private DateTimeGenerator dateTimeGenerator;
    private EntryProcessor defaultEntryProcessor;
    private LocalDateTime currentDateTime;
    private String currentTsoCode;
    private ArrayList<String> sshAndTpIdList;
    private String eqBdId;
    private String tpBdId;

    private ZipOutputStream outerZipOutputStream;

    private ZipOutputStream getNewInnerZipOutputStream(String name) {
        try {
            outerZipOutputStream.putNextEntry(new ZipEntry(name));
            return new ZipOutputStream(outerZipOutputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create inner zip file: " + name, e);
        }


    }

    public Generator(Configuration configuration) {
        this.configuration = configuration;
    }

    private Configuration configuration;

    public void generate() {
        LOGGER.debug("starting");

        createOuterZip();

        inputFiles = Arrays.asList(
                new InputFile(configuration.getEqInputFileName(), FileType.EQ),
                new InputFile(configuration.getSshInputFileName(), FileType.SSH),
                new InputFile(configuration.getTpInputFileName(), FileType.TP));

        eqBdId = UUID.randomUUID().toString();
        tpBdId = UUID.randomUUID().toString();

        dateTimeGenerator = new DateTimeGenerator(configuration);

        defaultEntryProcessor = new DefaultEntryProcessor();

        generateBoundarySetFiles();

        while (dateTimeGenerator.hasNext()) {
            currentDateTime = dateTimeGenerator.next();
            generateFilesForCurrentTime();
        }
        closeOuterZip();
    }

    private void closeOuterZip() {
        try {
            outerZipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createOuterZip() {
        try {
            outerZipOutputStream = new ZipOutputStream(new FileOutputStream(configuration.getOutputFile()));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Couldn't create output zip file.", e);

        }
    }

    private void generateBoundarySetFiles() {

        LocalDateTime scenarioDateTime = LocalDateTime.of(configuration.getScenarioDate(), LocalTime.MIDNIGHT.plusMinutes(30));

        String fileNameStub = scenarioDateTime.format(configuration.getDateTimeFormatterForFileNaming());
        String zipFileName = String.format("%s_2D_BD_%03d.zip", fileNameStub, configuration.getVersion());
        String eqBdFileName = String.format("%s_2D_EQ_BD_%03d.xml", fileNameStub, configuration.getVersion());
        String tpBdFileName = String.format("%s_2D_TP_BD_%03d.xml", fileNameStub, configuration.getVersion());


        try (BufferedReader eqBdReader = new BufferedReader(new FileReader(configuration.getEqBdInputFileName()));
                BufferedReader tpBdReader = new BufferedReader(new FileReader(configuration.getTpBdInputFileName()))) {

            ZipOutputStream zipOutputStream = getNewInnerZipOutputStream(zipFileName);


            // tp bd
            String refId = eqBdId;
            DependentOnProcessor dependentOnProcessor = new DependentOnProcessor(refId);


            PrintWriter writer = new PrintWriter(zipOutputStream);
            zipOutputStream.putNextEntry(new ZipEntry(eqBdFileName));

            generateBoundarySetFile(eqBdReader, writer, new ArrayList<>(
                    Arrays.asList(
                            new AboutProcessor(eqBdId),
                            new VersionProcessor(configuration.getVersion()),
                            new ScenarioTimeProcessor(scenarioDateTime))));


            writer.flush();
            zipOutputStream.closeEntry();

            zipOutputStream.putNextEntry(new ZipEntry(tpBdFileName));


            generateBoundarySetFile(tpBdReader, writer, new ArrayList<>(
                    Arrays.asList(
                            new DependentOnProcessor(eqBdId),
                            new AboutProcessor(tpBdId),
                            new VersionProcessor(configuration.getVersion()),
                            new ScenarioTimeProcessor(scenarioDateTime))));


            writer.flush();

            zipOutputStream.closeEntry();
            zipOutputStream.finish();
            outerZipOutputStream.closeEntry();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void generateBoundarySetFile(BufferedReader reader, PrintWriter writer, List<EntryProcessor> processors) throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            String processedLine = null;
            for (EntryProcessor processor : processors) {
                if (processor.canProcess(line)) {
                    processedLine = processor.process(line);
                    processors.remove(processor);
                    break;
                }
            }
            writer.println(processedLine != null ? processedLine : line);


        }


    }

    private void generateFilesForCurrentTime() {
        LOGGER.debug("generateFilesForCurrentTime " + currentDateTime );
        sshAndTpIdList = new ArrayList<String>();
        for (String tsoCode : configuration.getTsoCodes()) {
            currentTsoCode = tsoCode;
            generateFilesForCurrentTSO();
        }
        generateSVFile();
    }


    private class SVDependentOnProcessor implements EntryProcessor {

        private static final String REGEXP = "(\\s*<md:Model.DependentOn rdf:resource=\").*?(\"\\s*/>.*)";
        private final Pattern PATTERN = Pattern.compile(REGEXP);


        private boolean alreadyInvoked = false;

        public boolean canProcess(String entry) {
            return PATTERN.matcher(entry).matches();
        }

        public String process(String entry) {
            if (alreadyInvoked) {
                return "";
            }
            alreadyInvoked = true;

            StringBuilder stringBuilder = new StringBuilder();

            List<String> references = new ArrayList<>(sshAndTpIdList);
            references.add(tpBdId);

            for (String id : references) {
                stringBuilder.append(entry.replaceAll(REGEXP, String.format("$1urn:uuid:_%s$2", id))).append("\n");
            }


            return stringBuilder.toString();

        }
    }

    private void generateSVFile() {
        List<EntryProcessor> processors = new CopyOnWriteArrayList<>();

        processors.add(new ScenarioTimeProcessor(currentDateTime));
        processors.add(new VersionProcessor(configuration.getVersion()));
        processors.add(new AboutProcessor(UUID.randomUUID().toString()));

        EntryProcessor dependentOnProcessor = new SVDependentOnProcessor();

        String outputFileName = String.format(
                "%s_2D_CGMCE_SV_%03d",
                currentDateTime.format(configuration.getDateTimeFormatterForFileNaming()),
                configuration.getVersion());
        LOGGER.debug("Gonna write SV  " + outputFileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(configuration.getSvInputFileName()))) {

            ZipOutputStream zipOutputStream = getNewInnerZipOutputStream(outputFileName + ".zip");
            zipOutputStream.putNextEntry(new ZipEntry(outputFileName + ".xml"));
            PrintWriter writer = new PrintWriter(zipOutputStream);

            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = null;
                if (processors.size() > 0) {
                    for (EntryProcessor processor : processors) {
                        if (processor.canProcess(line)) {
                            processedLine = processor.process(line);
                            processors.remove(processor);
                            break;
                        }

                    }
                }
                if (processedLine == null) {
                    if (dependentOnProcessor.canProcess(line)) {
                        processedLine = dependentOnProcessor.process(line);
                    } else {
                        processedLine = defaultEntryProcessor.process(line);
                    }
                }
                writer.println(processedLine);
            }
            writer.flush();
            zipOutputStream.closeEntry();
            zipOutputStream.finish();
            outerZipOutputStream.closeEntry();


        } catch (IOException e) {
            throw new IllegalStateException("Couldn't write " + outputFileName + ".xml", e);
        }

    }

    private void generateFilesForCurrentTSO() {
        String eqUid = UUID.randomUUID().toString();
        for (InputFile inputFile : inputFiles) {
            LOGGER.debug(String.format("Generating %s %s outputs for %s", currentTsoCode, inputFile.getType(), currentDateTime));

            List<EntryProcessor> processors = new CopyOnWriteArrayList<>();

            processors.add(new ScenarioTimeProcessor(currentDateTime));
            processors.add(new VersionProcessor(configuration.getVersion()));

            String currentUid;
            if (!inputFile.isEQFile()) {
                currentUid = UUID.randomUUID().toString();
                sshAndTpIdList.add(currentUid);
                processors.add(new DependentOnProcessor(eqUid));
            } else {
                currentUid = eqUid;
                processors.add(new DependentOnProcessor(eqBdId));
            }

            processors.add(new AboutProcessor(currentUid));

            String fileNameStub = String.format(inputFile.getType().getFileNameStub(), currentTsoCode);

            String outputFileName = String.format(
                    "%s_%s_%03d",
                    currentDateTime.format(configuration.getDateTimeFormatterForFileNaming()),
                    fileNameStub,
                    configuration.getVersion());




            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile.getFile()))) {

                ZipOutputStream zipOutputStream =getNewInnerZipOutputStream(outputFileName + ".zip");
                PrintWriter writer = new PrintWriter(zipOutputStream);
                zipOutputStream.putNextEntry(new ZipEntry(outputFileName + ".xml"));


                String line;
                while ((line = reader.readLine()) != null) {
                    String processedLine = null;
                    if (processors.size() > 0) {
                        for (EntryProcessor processor : processors) {
                            if (processor.canProcess(line)) {
                                processedLine = processor.process(line);
                                processors.remove(processor);
                                break;
                            }

                        }
                    }
                    if (processedLine == null) {
                        processedLine = defaultEntryProcessor.process(line);
                    }
                    writer.println(processedLine);
                }
                writer.flush();
                zipOutputStream.closeEntry();
                zipOutputStream.finish();
                outerZipOutputStream.closeEntry();

            } catch (IOException e) {
                throw new IllegalStateException("Couldn't write " + outputFileName + ".xml", e);
            }
        }
    }


}
