package org.occideas.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.exceptions.GenericException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    private static Logger log = LogManager.getLogger(FileUtil.class);

    public static boolean saveJsonToFile(String json, String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir.getPath() + FileSystems.getDefault().getSeparator() + filename + ".json");
        if (file.exists()) {
            log.info("expected file - " + file.getAbsolutePath() + " already exist.");
        }
        log.info("[Start] writing to file - " + directory);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public static String readJsonFile(String path) {
        Path fileName
                = Path.of(path);
        try {
            String json = Files.readString(fileName);
            return json;
        } catch (IOException e) {
            throw new GenericException("No file exist - " + fileName);
        }
    }

}
