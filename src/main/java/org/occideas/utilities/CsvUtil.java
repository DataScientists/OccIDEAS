package org.occideas.utilities;

import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.occideas.voxco.model.Question;

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
        //readVoxcoResponse();
        readTranslations();
    }

    private static void readVoxcoResponse() {
        try {
            System.out.println(new Date(0));
            String csvPath = "/tmp/test-3.csv";
            List<String[]> extract = readAll(csvPath);
            Map<String, Map<String, String>> formatted = new LinkedHashMap<>();
            String[] labels = extract.get(0);
            int index = 0;
            for (String[] data : extract) {
                if (index > 0) {
                    Map<String, String> entry = new LinkedHashMap<>();
                    int dataIndex = 0;
                    for (String value : data) {
                        if (dataIndex > 26) {
                            entry.put(labels[dataIndex], value);
                        }
                        dataIndex++;
                    }
                    formatted.put("MKEY_" + data[0] + "__" + data[4], entry);
                }
                index++;
            }
            System.out.println(formatted);
            formatted.forEach((key, answers) -> {
                String[] keys = key.split("__");
                System.out.println("size: " + keys.length);
                if (keys != null && keys.length == 2) {
                    System.out.println("keys[1]: " + keys[1]);
                }
            });
            /*String moduleKey = "WELD";
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
