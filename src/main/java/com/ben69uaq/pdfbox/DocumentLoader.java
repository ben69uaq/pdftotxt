package com.ben69uaq.pdfbox;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

public class DocumentLoader {

    DocumentLoader() {
    }

    PDDocument loadDocument(File file) {
        try {
            return Loader.loadPDF(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}