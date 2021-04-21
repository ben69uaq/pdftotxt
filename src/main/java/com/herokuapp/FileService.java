package com.herokuapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileService {

    private final static String ROOT = "/data/";

    static File getFile(String fileName) {
        return new File(ROOT + "input/" + fileName);
    }

    static List<String> listFile() {
        final String inputPath = ROOT + "input/";
        try {
            return Files.list(Paths.get(inputPath))
            .filter(file -> !Files.isDirectory(file))
            .map(Path::getFileName)
            .map(Path::toString)
            .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    static void storeTxt(final String fileName, final String content) {
        final String outputPath = ROOT + "output/" + fileName;
        try {
            Files.write(Paths.get(outputPath), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void storeFile(final MultipartFile file) {
        final String inputPath = ROOT + "input/";
        try {
            file.transferTo(new File(inputPath + file.getOriginalFilename()));
            log.info("File stored: <" + file.getOriginalFilename() +">");
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

}