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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author sujay
 */
public class StringBuilderJavaSourceTest {
    
    public StringBuilderJavaSourceTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCharContent method, of class StringBuilderJavaSource.
     */
    @Test
    public void testGetCharContent() throws Exception {
        boolean ignoreEncodingErrors = false;
        StringBuilderJavaSource instance = new StringBuilderJavaSource("sourcecode1.java");
        instance.append("Hello World");
        CharSequence expResult = "Hello World\n";
        CharSequence result = instance.getCharContent(ignoreEncodingErrors);
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of append method, of class StringBuilderJavaSource.
     */
    @Test
    public void testAppend() throws Exception{
        boolean ignoreEncodingErrors = false;
        String code = "Hello World";
        StringBuilderJavaSource instance = new StringBuilderJavaSource("sourcecode1.java");
        instance.append(code);
        CharSequence result = instance.getCharContent(ignoreEncodingErrors);
        CharSequence expResult = "Hello World\n";
        assertEquals(expResult.toString(), result.toString());
    }
}