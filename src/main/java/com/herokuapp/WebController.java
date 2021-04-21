package com.herokuapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import static java.util.stream.Collectors.toList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebController {

    DocumentService documentService = new DocumentService();

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<String> index() throws IOException {
        Path filePath = Paths.get("src/main/resources/index.html");
        String fileContent = new String(Files.readAllBytes(filePath));
        return ResponseEntity.ok().body(fileContent);
    }

    @GetMapping("list")
    @ResponseBody
    public ResponseEntity<String> list() throws IOException {
        List<String> fileList = FileService.listFile().stream()
        .map(WebController::encode)
        .collect(toList());
        return ResponseEntity.ok().body(fileList.toString());
    }

    @RequestMapping("file/{fileName}")
    @ResponseBody
    public ResponseEntity<String> file(@PathVariable String fileName) throws IOException {
        String decodedFileName = URLDecoder.decode(fileName, "UTF_8");
        String content = documentService.getFileContent(decodedFileName);
        log.info("reading file <" + decodedFileName + ">");
        return ResponseEntity.ok().body(content.replaceAll("\r\n", "<br/>"));
    }

    @RequestMapping("download")
    @ResponseBody
    public ResponseEntity<String> dowload() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("upload")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void upload(@RequestParam("file") MultipartFile file) {
        log.info("uploading file <" + file.getOriginalFilename() + ">");
        FileService.storeFile(file);
    }

    /*
     * Not needed with Java10+
     */
    static private String encode(String input) {
        try {
            return "\"" + URLEncoder.encode(input, "UTF_8") + "\"";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}