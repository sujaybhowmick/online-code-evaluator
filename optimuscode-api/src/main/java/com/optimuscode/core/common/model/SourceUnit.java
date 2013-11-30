package com.optimuscode.core.common.model;

import com.optimuscode.core.common.Janitor;

import java.io.*;

public class SourceUnit extends ProcessingUnit{
    private String name;
    private StringReaderSource sourceCode;
    private String extension;

    public SourceUnit(final Object source, final String name,
                      final String sourceCode,
                      final String extension){
        super(source);
        this.name = name;
        this.sourceCode = new StringReaderSource(sourceCode);
        this.extension = extension;
    }


    public String getName() {
        return name;
    }

    public String getSource() {
        try{
            StringBuilder buffer = new StringBuilder();
            String line;
            int lineNumber = 1;
            while((line = sourceCode.getLine(lineNumber, new Janitor())) != null){
                buffer.append(line).append("\n");
                lineNumber++;
            }
            return buffer.toString();
        }finally{
            sourceCode.cleanup();
        }
    }

    public String getExtension(){
        return this.extension;
    }

    public void write(final String sourceDir){
        String javaFileName =
                sourceDir + File.separatorChar + getName() +
                        getExtension();
        FileWriter writer = null;

        try {
            writer = new FileWriter(new File(javaFileName));
            writer.write(getSource());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
