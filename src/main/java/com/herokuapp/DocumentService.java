package com.herokuapp;

import java.awt.Rectangle;
import java.io.File;
import java.util.Collections;

import org.apache.pdfbox.pdmodel.PDDocument;

public class DocumentService {

    DocumentReader documentReader;

    DocumentService() {
        documentReader = new DocumentReader(new Rectangle(23, 55, 548, 660), Collections.emptyList());
    }

    void processAllFiles() {
        FileService.listFile().stream().forEach(this::processOneFile);
    }

    private void processOneFile(final String fileName) {
        String content = getFileContent(fileName);
        FileService.storeTxt(fileName.replace(".pdf", ".txt"), content);
    }

    String getFileContent(final String fileName) {
        File file = FileService.getFile(fileName);
        PDDocument document = DocumentLoader.loadDocument(file);
        String content = documentReader.read(document);
        return DocumentSanitizer.sanitize(content);
    }
}