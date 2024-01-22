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

    private final String inputPath;
    private final String outputPath;

    public FileService(String root, String sessionPath) {
        inputPath = root + "/" + sessionPath + "/input/";
        outputPath = root + "/" + sessionPath + "/output/";
        initialize(root, sessionPath);
    }

    private void initialize(String root, String sessionPath) {
        Path input = Paths.get(root + "/" + sessionPath+ "/input");
        Path output = Paths.get(root + "/" + sessionPath+ "/output");
        try {
            Files.createDirectories(input);
            Files.createDirectories(output);
        } catch (IOException e) {
            log.info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public List<String> list() {
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
        try {
            Files.write(Paths.get(outputPath + fileName), content.getBytes());
            log.info("Text stored: <" + fileName +">");
        } catch (IOException e) {
            log.info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void storePdf(final MultipartFile file) {
        try {
            file.transferTo(new File(inputPath + file.getOriginalFilename()));
            log.info("Pdf stored: <" + file.getOriginalFilename() +">");
        } catch (IllegalStateException | IOException e) {
            log.info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public File getPdf(String fileName) {
        return new File(inputPath + fileName);
    }

    public void storeImage(final String fileName, final byte[] file) {
        String imageName = fileName.replace(".pdf", ".png");
        try {
            Files.write(Paths.get(outputPath + imageName), file);
            log.info("Image stored: <" + imageName +">");
        } catch (IOException e) {
            log.info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public byte[] getImage(String fileName) {
        String imageName = fileName.replace(".pdf", ".png");
        try {
            log.info("Image retrieved: <" + imageName +">");
            return Files.readAllBytes(Paths.get(outputPath + imageName));
        } catch (IOException e) {
            log.info(e.getClass().getName() + ": " + e.getMessage());
        }
        return new byte[0];
    }

}