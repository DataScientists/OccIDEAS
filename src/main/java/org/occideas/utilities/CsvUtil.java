package org.occideas.utilities;

import com.opencsv.CSVReader;

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
        try {
            System.out.println(new Date(0));
            String csvPath = "/tmp/voxco_result_weld_welder_5815_extract/weld_welder_5815.csv";
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
                            String label = labels[dataIndex].replaceAll("\\{", "")
                                    .replaceAll("\\}", "");
                            entry.put(label, value);
                        }
                        dataIndex++;
                    }
                    formatted.put("CASEID_" + data[0], entry);
                }
                index++;
            }
            System.out.println(formatted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
