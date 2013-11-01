package com.optimuscode.core.utils;

public enum SupportedLanguage {
    Java("java"), Groovy("groovy"), C("c"), Cpp("cpp");

    String language;
    SupportedLanguage(String language){
        this.language = language;

    }

    public static String[] supportedLanguageLists(){
        SupportedLanguage[] values = SupportedLanguage.values();
        String[] languages = new String[values.length];
        for (int i = 0; i < values.length; i++){
            languages[i] = values[i].name();
        }
        return languages;
    }

    @Override
    public String toString() {
        return this.language;
    }
}
