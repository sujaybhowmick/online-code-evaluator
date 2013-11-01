package com.optimuscode.core.java.compiler;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 9/9/13
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface JavaCompileResult {
    public void execute(DiagnosticCollector<JavaFileObject> d);
    public String getClassOutputLocation();
}
