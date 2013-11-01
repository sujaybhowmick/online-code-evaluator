package com.optimuscode.core.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SupportedLanguageTest {
    @Test
    public void testGetSupportedLanguages() throws Exception {
        SupportedLanguage java = SupportedLanguage.Java;
        SupportedLanguage groovy = SupportedLanguage.Groovy;
        SupportedLanguage c = SupportedLanguage.C;
        SupportedLanguage cpp = SupportedLanguage.Cpp;

        assertEquals("java", java.toString());
        assertEquals("groovy", groovy.toString());
        assertEquals("c", c.toString());
        assertEquals("cpp", cpp.toString());
    }
}
