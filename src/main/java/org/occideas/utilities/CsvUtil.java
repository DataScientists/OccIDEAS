package org.occideas.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.occideas.voxco.model.Question;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvUtil {

    private static final String RESPONSE_KEY_SEPARATOR = "__";
    private static final String INPUT_DIRECTORY = "/temp/";
    private static final String NODEKEY_GENE = "GENE";
    private static final String NO_PIN = "NO-PIN";

    public static List<String[]> readAll(String csvPath) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(csvPath));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

    public static void main(String[] args) {
        readVoxcoResponse("AIRT", INPUT_DIRECTORY + "airt.csv");
        //readTranslations();
    }

    private static void readVoxcoResponse(String moduleKey, String csvPath) {
        try {
            System.out.println(new Date(0));
            int dataStartIndex = NODEKEY_GENE.equals(moduleKey) ? 30 : 32;
            int occupationIndex = NODEKEY_GENE.equals(moduleKey) ? 30 : 31;
            List<String[]> extract = readAll(csvPath);
            Map<String, Map<String, String>> formatted = new LinkedHashMap<>();
            String[] labels = extract.get(0);
            int index = 0;
            for (String[] data : extract) {
                if (index > 0) {
                    Map<String, String> entry = new LinkedHashMap<>();
                    int dataIndex = 0;
                    for (String value : data) {
                        if (dataIndex > dataStartIndex) {
                            entry.put(labels[dataIndex], value);
                        }
                        dataIndex++;
                    }
                    String pin = data[4];
                    if ("".equals(pin) || pin == null) {
                        pin = NO_PIN;
                    }
                    formatted.put(moduleKey + "_" + data[0] + RESPONSE_KEY_SEPARATOR + pin + RESPONSE_KEY_SEPARATOR + data[occupationIndex], entry);
                }
                index++;
            }
            System.out.println(formatted);
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new File(INPUT_DIRECTORY + moduleKey + ".json"), formatted);
            /*formatted.forEach((key, answers) -> {
                String[] keys = key.split("__");
                System.out.println("size: " + keys.length);
                if (keys != null && keys.length == 2) {
                    System.out.println("keys[1]: " + keys[1]);
                }
            });
            String moduleKey = "WELD";
            formatted.forEach((caseId, answers) -> {
                answers.forEach((label, answer) -> {
                    if (answer != null && !StringUtils.EMPTY.equals(answer)) {
                        String[] qVariable = label.split("_");
                        String qType = qVariable[0];
                        String nodeKey = qVariable[1];
                        System.out.println("nodeKey=" + nodeKey);
                        if (qVariable.length == 4 && !nodeKey.equals(moduleKey)) {
                            System.out.println("linked");
                        } else if (qVariable.length == 3 && nodeKey.equals(moduleKey)) {
                            System.out.println("not linked");
                        }

                        if (Question.Type.CheckBox.getVariable().equals(qType)) {
                            String actualVariable = label.substring(0, label.lastIndexOf("_"));
                            System.out.println("CHECK: actualVariable=" + actualVariable);
                            String qNumber = actualVariable.substring(actualVariable.lastIndexOf("_") + 1);
                            System.out.println("CHECK: qNumber=" + qNumber);
                            System.out.println("CHECK: answer=" + answer);
                            System.out.println("CHECK: aNumber=" + answer.split("_")[1]);

                        } else if (Question.Type.RadioButton.getVariable().equals(qType)) {
                            System.out.println("RADIO: actualVariable=" + label);
                            String qNumber = label.substring(label.lastIndexOf("_") + 1);
                            System.out.println("RADIO: qNumber=" + qNumber);
                            System.out.println("RADIO: answer=" + answer);
                            System.out.println("RADIO: aNumber=" + answer.split("_")[1]);
                        }
                    }
                });
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readTranslations() {
        try {
            System.out.println(new Date(0));
            String csvPath = "/tmp/module_translations.csv";
            List<String[]> extract = readAll(csvPath);
            Map<String, Map<String, String>> formatted = new LinkedHashMap<>();
            String[] labels = extract.get(0);
            int index = 0;
            for (String[] data : extract) {
                if (index > 0) {
                    Map<String, String> entry = new LinkedHashMap<>();
                    int dataIndex = 0;
                    for (String value : data) {
                        if (dataIndex > 0) {
                            entry.put(labels[dataIndex], value);
                        }
                        dataIndex++;
                    }
                    formatted.put(data[0], entry);
                }
                index++;
            }
            System.out.println(formatted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
