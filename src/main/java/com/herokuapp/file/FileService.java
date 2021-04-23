package com.herokuapp.file;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileService {

    private final String root;
    //windows "c:/data/";
    //linux "/app/data/";

    public FileService(String rootPath) {
        root = rootPath;
        initilize();
    }

    public void initilize() {
        Path input = Paths.get(root + "input");
        Path output = Paths.get(root + "output");
        try {
            Files.createDirectories(input);
            Files.createDirectories(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File get(String fileName) {
        return new File(root + "input/" + fileName);
    }

    public List<String> list() {
        final String inputPath = root + "input/";
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

    public void storeTxt(final String fileName, final String content) {
        final String outputPath = root + "output/" + fileName;
        try {
            Files.write(Paths.get(outputPath), content.getBytes());
            log.info("File stored: <" + fileName +">");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeFile(final MultipartFile file) {
        final String inputPath = root + "input/";
        try {
            file.transferTo(new File(inputPath + file.getOriginalFilename()));
            log.info("File stored: <" + file.getOriginalFilename() +">");
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

}