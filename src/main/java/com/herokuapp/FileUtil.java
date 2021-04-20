package com.herokuapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FileUtil {

    private final static String ROOT = "src/test/resources/";

    static List<Path> listFile() {
        final String inputPath = ROOT + "input/";
        try {
            return Files.list(Paths.get(inputPath))
                .filter(file -> !Files.isDirectory(file))
                .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    static void storeTxt(final String fileName, final String content) {
        final String outputPath = ROOT + "output/" + fileName;
        try {
            Files.writeString(Paths.get(outputPath), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}