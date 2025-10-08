package org.occideas.admin;

import java.io.*;
import java.util.*;

public class InterviewProcessor {

    /**
     * Extracts interview records from a CSV file and returns them as a list of maps.
     * Each map represents one reshaped interview row.
     */
    public static List<Map<String, String>> extractInterviews(String filePath) {
        List<Map<String, String>> reshapedData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) return reshapedData;

            String[] headers = headerLine.split(",");
            Map<String, List<Integer>> interviewGroups = new HashMap<>();

            // Identify the index of record_id column
            int recordIdIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase("record_id")) {
                    recordIdIndex = i;
                    break;
                }
            }

            if (recordIdIndex == -1) {
                System.out.println("record_id column not found.");
                return reshapedData;
            }

            // Group columns by the first four characters
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].length() >= 4 && i != recordIdIndex) {
                    String prefix = headers[i].substring(0, 4);
                    interviewGroups.computeIfAbsent(prefix, k -> new ArrayList<>()).add(i);
                }
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (recordIdIndex >= values.length) continue;

                String recordId = values[recordIdIndex];
                int interviewCount = 0;

                for (Map.Entry<String, List<Integer>> entry : interviewGroups.entrySet()) {
                    List<Integer> columnIndexes = entry.getValue();
                    boolean hasValidData = false;

                    for (int index : columnIndexes) {
                        if (index < values.length && !values[index].equals("") && !values[index].equals("0")) {
                            hasValidData = true;
                            break;
                        }
                    }

                    if (hasValidData) {
                        interviewCount++;
                        String interviewId = recordId + "_" + interviewCount;

                        Map<String, String> rowMap = new LinkedHashMap<>();
                        rowMap.put("interviewId", interviewId);

                        for (int relatedIndex : columnIndexes) {
                            if (relatedIndex < values.length) {
                                String val = values[relatedIndex];
                                if (!val.equals("") && !val.equals("0")) {
                                    rowMap.put(headers[relatedIndex], val);
                                }
                            }
                        }
                        reshapedData.add(rowMap);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reshapedData;
    }

    /**
     * Writes reshaped data to a new CSV file in long format.
     */
    public static void writeLongCSV(List<Map<String, String>> data, String outputFilePath) {
        if (data.isEmpty()) {
            System.out.println("No data to write.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Collect all possible headers
            Set<String> headers = new LinkedHashSet<>();
            for (Map<String, String> row : data) {
                headers.addAll(row.keySet());
            }

            // Write header
            writer.write(String.join(",", headers));
            writer.newLine();

            // Write rows
            for (Map<String, String> row : data) {
                List<String> rowValues = new ArrayList<>();
                for (String header : headers) {
                    rowValues.add(row.getOrDefault(header, ""));
                }
                writer.write(String.join(",", rowValues));
                writer.newLine();
            }

            System.out.println("Long format CSV written to: " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wrapper that uses extractInterviews() and writes results to new CSV.
     */
    public static void processCSV(String inputFile, String outputFile) {
        List<Map<String, String>> data = extractInterviews(inputFile);
        writeLongCSV(data, outputFile);
    }

    public static void main(String[] args) {
        String inputPath = "/home/seven/Documents/occideas/test003.csv";
        String outputPath = "/home/seven/Documents/occideas/test003_long.csv";
        processCSV(inputPath, outputPath);
    }
}
