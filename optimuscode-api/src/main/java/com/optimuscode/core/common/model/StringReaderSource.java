package com.optimuscode.core.common.model;

import java.io.Reader;
import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 9/18/13
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringReaderSource extends AbstractReaderSource {
    final String string;

    public StringReaderSource(final String string){
        this.string = string;
    }

    @Override
    public Reader getReader() {
        return new StringReader(this.string);
    }
}
