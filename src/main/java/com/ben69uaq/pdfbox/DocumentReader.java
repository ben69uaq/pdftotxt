package com.ben69uaq.pdfbox;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.stream.IntStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class DocumentReader {

    private final String separator = System.getProperty("line.separator");

    private PDFTextStripperByArea stripper;

    DocumentReader() {
        try {
            stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String read(PDDocument document, Rectangle limit) {
        stripper.removeRegion("limit");
        stripper.addRegion("limit", limit);
        return readDocument(document);
    }

    private String readDocument(PDDocument document) {
        return IntStream.range(0, document.getNumberOfPages())
            .mapToObj(document::getPage)
            .map(this::readPage)
            .reduce("", String::concat);
    }

    private String readPage(final PDPage page) {
        try {
			stripper.extractRegions(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return stripper.getTextForRegion("limit").concat(separator);
    }

}