package com.jsonparser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestJsonParser {
    JsonParser jsonParser;

    @Before
    public void setUp() {
        jsonParser = new JsonParser();
    }

    @Test
    public void testValidFile(){
        assertTrue(jsonParser.parseJson("ips.json"));
    }

    @Test
    public void testUnknownParameter(){
        assertTrue(jsonParser.parseJson("fileWithAdditionalParameter.json"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileNotFound() {
        jsonParser.parseJson(" ");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFileWithInvalidValues() {
        jsonParser.parseJson("fileWithInvalidValues.json");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidFileName() {
        jsonParser.parseJson("fileWithInvalid.json");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFileWithNoData() {
        jsonParser.parseJson("fileWithNoData.json");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFileWithInvalidFormat() {
        jsonParser.parseJson("fileWithInvalidFormat.json");
    }
}
