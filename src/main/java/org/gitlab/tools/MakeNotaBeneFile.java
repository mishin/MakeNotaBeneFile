package org.gitlab.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

class MakeNotaBeneFile {
    private static final String PROP_FILENAME = "config.properties";
    private final String working_directory;

    private MakeNotaBeneFile(Properties prop) {
        working_directory = prop.getProperty("working_directory");
    }

    /**
     * Правим уровень логирования в ESB процессе
     */
    public static void main(String[] args) throws IOException {
        MakeNotaBeneFile MakeNotaBeneFile = new MakeNotaBeneFile(readProperties());
        MakeNotaBeneFile.createDirAndNotaBeneFile();
    }

    private void createDirAndNotaBeneFile() {
        // Check If Directory Already Exists Or Not?
        Path dirPathObj = Paths.get(working_directory + "/" + getCurrentLocalDateTime());
        Path filedirPathObj = Paths.get(working_directory + "/" + getCurrentLocalDateTime() + "/" + getCurrentLocalDateTime() + "_nb.txt");
        if (Files.exists(dirPathObj)) {
            System.out.println("! Directory " + dirPathObj + " Already Exists !");
        } else {
            try {
                // Creating The New Directory Structure
                Files.createDirectories(dirPathObj);
                System.out.println("! New Directory " + dirPathObj + " Successfully Created !");
                Files.createFile(filedirPathObj);
                System.out.println("! New file " + filedirPathObj + " Successfully Created !");
            } catch (FileAlreadyExistsException x) {
                System.err.format("file named %s" +
                        " already exists%n", filedirPathObj);
            } catch (IOException ioExceptionObj) {
                System.out.println("Problem Occured While Creating The Directory Structure= " + ioExceptionObj.getMessage());
            }
        }
    }


    private String getCurrentLocalDateTime() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("ddMMyyyy"));
    }

    static private Properties readProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(PROP_FILENAME);
            // load a properties file
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
}
