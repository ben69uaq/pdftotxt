package com.herokuapp.pdfbox;

import java.awt.Rectangle;
import java.io.File;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

public class DocumentService {

    private final DocumentLoader documentLoader;
    private final DocumentReader documentReader;
    private final DocumentConverter documentConverter;

    public DocumentService() {
        documentLoader = new DocumentLoader();
        documentReader = new DocumentReader();
        documentConverter = new DocumentConverter();
    }

    public byte[] convert(final File file) {
        PDDocument document = documentLoader.loadDocument(file);
        byte[] content = documentConverter.convert(document);
        return content;
    }

    public String read(final File file, final String layout) {
        PDDocument document = documentLoader.loadDocument(file);
        String content = documentReader.read(document, extractLimit(layout));
        return content;
    }

    private Rectangle extractLimit(final String layout) {
        if(StringUtils.isNotBlank(layout)) {
            final int[] limits = Stream.of(layout.split("_")).mapToInt(Integer::parseInt).toArray();
            return new Rectangle(limits[0], limits[1], limits[2], limits[3]);
        }
        return new Rectangle(23, 55, 600, 660); // default limit;
    }
}