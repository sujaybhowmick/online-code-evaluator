package com.optimuscode.core.common.model;

import com.optimuscode.core.common.Janitor;

public class SourceUnit extends ProcessingUnit{
    private String name;
    private StringReaderSource sourceCode;

    public SourceUnit(final Object source, final String name, final String sourceCode){
        super(source);
        this.name = name;
        this.sourceCode = new StringReaderSource(sourceCode);
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
}
