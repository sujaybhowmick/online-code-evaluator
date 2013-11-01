package com.optimuscode.core.common.model;

import com.optimuscode.core.common.Janitor;

import java.io.BufferedReader;
import java.io.IOException;
/**
 * Inspired by Groovy's compiler API
 */
public abstract class AbstractReaderSource implements ReaderSource {
    private BufferedReader lineSource  = null;
    private String line = null;
    private int number = 0;

    @Override
    public String getLine(int lineNumber, final Janitor janitor) {
        if (lineSource != null && number > lineNumber) {
            cleanup();
        }

        // If the line source is closed, try to open it.
        if (lineSource == null) {
            try {
                lineSource = new BufferedReader(getReader());
            } catch (Exception e) {
                // Ignore
            }
            number = 0;
        }
        // Read until the appropriate line number.
        if (lineSource != null) {
            while (number < lineNumber) {
                try {
                    line = lineSource.readLine();
                    number++;
                }
                catch (IOException e) {
                    cleanup();
                }
            }
            if (janitor == null) {
                final String result = line;   // otherwise cleanup() will wipe out value
                cleanup();
                return result;
            } else {
                janitor.register(this);
            }

        }
        return line;
    }

    @Override
    public void cleanup() {
        if (lineSource != null) {
            try {
                lineSource.close();
            } catch (Exception e) {
                // Ignore
            }
        }

        lineSource = null;
        line = null;
        number = 0;
    }
}
