package com.herokuapp;

import org.junit.Test;

public class DocumentServiceTest {

    //@Test
    public void shouldConvertToTxt() throws Exception {
        new DocumentService().processAllFiles();
    }
}