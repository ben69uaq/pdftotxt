package com.herokuapp;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import com.herokuapp.file.FileService;
import com.herokuapp.pdfbox.DocumentService;
import com.herokuapp.sanitize.SanitizerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class WebController {
    
    private final String separator = System.getProperty("line.separator");

    private final FileService fileService;
    private final DocumentService documentService;
    private final SanitizerService sanitizerService;

    WebController(@Value("${root}") String root) {
        fileService = new FileService(root);
        documentService = new DocumentService();
        sanitizerService = new SanitizerService();
        fileService.initilize();
    }

    @GetMapping("list")
    @ResponseBody
    public ResponseEntity<String> list() {
        List<String> fileList = fileService.list().stream().map(WebController::encode).collect(toList());
        return ResponseEntity.ok().body(fileList.toString());
    }

    @RequestMapping("delete/{fileName}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void delete(@PathVariable String fileName) {
        log.info("deleting file <" + fileName + ">");
        Optional.of(fileName)
            .map(WebController::decode)
            .map(fileService::get)
            .map(File::delete);
    }

    @RequestMapping("delete/all")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteAll() {
        log.info("deleting all files");
        fileService.list().stream()
            .map(fileService::get)
            .forEach(File::delete);
    }

    @RequestMapping("get/{fileName}")
    @ResponseBody
    public ResponseEntity<String> get(@PathVariable String fileName, @RequestParam String rules) {
        log.info("reading file with sanitization <" + fileName + ">");
        return Optional.of(fileName)
            .map(WebController::decode)
            .map(fileService::get)
            .map(file -> documentService.read(file, rules))
            .map(doc -> sanitizerService.sanitize(doc, rules))
            .map(content -> content.replaceAll(separator, "<br/>"))
            .map(content -> ResponseEntity.ok().body(content))
            .orElseThrow(() -> new RuntimeException("Not able to retrieve sanitized content of file " + fileName));
    }

    @RequestMapping("get/all")
    @ResponseBody
    public ResponseEntity<String> getAll(@RequestParam String rules) {
        log.info("reading all files with sanitization");
        return fileService.list().stream()
            .map(fileService::get)
            .map(file -> documentService.read(file, rules))
            .map(doc -> sanitizerService.sanitize(doc, rules))
            .reduce((a,b) -> a.concat(separator).concat("-----").concat(separator).concat(b))
            .map(content -> content.replaceAll(separator, "<br/>"))
            .map(content -> ResponseEntity.ok().body(content))
            .orElseThrow(() -> new RuntimeException("Not able to retrieve sanitized content of all file"));
    }

    @PostMapping("upload")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void upload(@RequestParam("file") MultipartFile file) {
        log.info("uploading file <" + file.getOriginalFilename() + ">");
        fileService.storeFile(file);
    }

    static private String encode(String input) {
        try {
            return "\"" + URLEncoder.encode(input, StandardCharsets.UTF_8.toString()) + "\"";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    static private String decode(String input) {
        try {
            return URLDecoder.decode(input,  StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}