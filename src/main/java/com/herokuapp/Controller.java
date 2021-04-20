package com.herokuapp;

import java.awt.Rectangle;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

public class Controller {

    DocumentReader documentReader;

    Controller() {
        documentReader = new DocumentReader(new Rectangle(0, 0, 500, 500), Collections.emptyList());
    }

    void process() throws Exception {
        System.out.println("process: begin");
        FileUtil.listFile().stream().forEach(this::processFile);
        System.out.println("process: end process");
    }

    private void processFile(final Path filePath) {
        Stream.of(DocumentLoader.loadDocument(filePath.toFile()))
            .map(documentReader::read)
            .map(DocumentSanitizer::sanitize)
            .forEach(txt -> FileUtil.storeTxt(filePath.getFileName().toString().replace(".pdf", ".txt"), txt));
    }
}