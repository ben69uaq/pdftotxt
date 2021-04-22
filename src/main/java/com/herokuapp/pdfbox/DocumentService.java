package com.herokuapp.pdfbox;

import java.awt.Rectangle;
import java.io.File;
import java.util.Collections;

import org.apache.pdfbox.pdmodel.PDDocument;

public class DocumentService {

    private final DocumentLoader documentLoader;
    private final DocumentReader documentReader;

    public DocumentService() {
        documentLoader = new DocumentLoader();
        documentReader = new DocumentReader(new Rectangle(23, 55, 548, 660), Collections.emptyList());
    }

    public String read(final File file) {
        PDDocument document = documentLoader.loadDocument(file);
        String content = documentReader.read(document);
        return content;
    }
}