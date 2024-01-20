package com.herokuapp;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.herokuapp.file.FileService;
import com.herokuapp.pdfbox.DocumentService;
import com.herokuapp.sanitize.SanitizerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebController {
    
    private final String separator = System.getProperty("line.separator");

    private final FileService fileService;
    private final DocumentService documentService;
    private final SanitizerService sanitizerService;

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
            .map(fileService::getPdf)
            .map(File::delete);
    }

    @RequestMapping("delete/all")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteAll() {
        log.info("deleting all files");
        fileService.list().stream()
            .map(fileService::getPdf)
            .forEach(File::delete);
    }

    @RequestMapping("get/{fileName}")
    @ResponseBody
    public ResponseEntity<String> get(@PathVariable String fileName, @RequestParam String rules) {
        log.info("reading file with sanitization <" + fileName + ">");
        return Optional.of(fileName)
            .map(WebController::decode)
            .map(fileService::getPdf)
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
            .map(fileService::getPdf)
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
        fileService.storePdf(file);
        Optional.of(file.getOriginalFilename())
            .map(WebController::decode)
            .map(fileService::getPdf)
            .map(documentService::convert)
            .ifPresent(c -> fileService.storeImage(file.getOriginalFilename(), c));
    }

    @RequestMapping("preview/{fileName}")
    @ResponseBody
    public ResponseEntity<byte[]> preview(@PathVariable String fileName) {
        log.info("preview file <" + fileName + ">");
        return Optional.of(fileName)
            .map(WebController::decode)
            .map(fileService::getImage)
            .map(content -> ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(content))
            .orElseThrow(() -> new RuntimeException("Not able to get preview of file " + fileName));
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