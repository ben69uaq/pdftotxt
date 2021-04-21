package com.herokuapp;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class DocumentReader {

    PDFTextStripperByArea stripper;
    List<Integer> excludedPages;

    DocumentReader(final Rectangle limit, final List<Integer> excludedPages) {
        this.excludedPages = excludedPages;
        try {
            stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(false);
            stripper.addRegion("limit", limit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String read(PDDocument document) {
        return IntStream.range(0, document.getNumberOfPages())
            .filter(pageIndex -> !excludedPages.contains(pageIndex))
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
        return stripper.getTextForRegion("limit").concat("\r\n");
    }

}