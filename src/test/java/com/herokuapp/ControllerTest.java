package com.herokuapp;

import org.junit.Test;

public class ControllerTest {

    @Test
    public void shouldConvertToTxt() throws Exception {
        new Controller().process();
    }
}