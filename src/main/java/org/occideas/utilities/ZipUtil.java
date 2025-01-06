package org.occideas.utilities;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class ZipUtil {

    public static void unzip(File file,String destination) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        File destDir = new File(destination);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        return destFile;
    }
    public static void zipFolder(String folderPath,String zipFilePath) throws IOException {
        Path sourceFolderPath = Paths.get(folderPath);
        //String zipFilePath = folderPath+".zip";

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            Files.walk(sourceFolderPath)
                    .filter(path -> !Files.isDirectory(path)) // Only include files, not directories
                    .forEach(path -> {
                        String zipEntryName = sourceFolderPath.relativize(path).toString();
                        try {
                            zos.putNextEntry(new ZipEntry(zipEntryName));
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

}
