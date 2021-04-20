package com.herokuapp;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

public class DocumentLoader {
    static PDDocument loadDocument(File file) {
        try {
            return Loader.loadPDF(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}