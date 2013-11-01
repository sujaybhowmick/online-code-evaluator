package com.optimuscode.core.common.model;

import com.optimuscode.core.common.HasCleanup;
import com.optimuscode.core.common.Janitor;

import java.io.Reader;

public interface ReaderSource extends HasCleanup {
    public String getLine(int lineNumber, final Janitor janitor);

    public void cleanup();

    public Reader getReader();
}
