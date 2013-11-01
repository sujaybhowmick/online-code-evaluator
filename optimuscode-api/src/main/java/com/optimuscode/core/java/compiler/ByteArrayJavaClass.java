/**
 * Copyright 2013 #name#
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimuscode.core.java.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 *
 * @author sujay
 */
public class ByteArrayJavaClass extends SimpleJavaFileObject{
    
    private ByteArrayOutputStream stream;
    private String className;
    
    public ByteArrayJavaClass(final String name){
        super(URI.create("bytes:///" + name), Kind.CLASS);
        this.stream = new ByteArrayOutputStream();
        this.className = name;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.stream;
    }
    
    public byte[] getBytes(){
        return this.stream.toByteArray();
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }
}
