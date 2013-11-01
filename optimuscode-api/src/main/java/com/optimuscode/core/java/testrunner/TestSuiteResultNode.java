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

package com.optimuscode.core.java.testrunner;


import java.util.EventObject;

/**
 *
 * @author sujay
 */
public class TestSuiteResultNode extends EventObject{

    private int errorCount = 0;
    private int failureCount = 0;
    private int testCaseCount = 0;
    private int successCount = 0;
    private double runTime = 0.0;
    private String suiteName;
    
    private TestSuiteResultNode(final Object source, final String suiteName){
        super(source);
        this.suiteName = suiteName;
    }
    
    private TestSuiteResultNode(final Object source){
        super(source);
    }
    
    
    
    public static TestSuiteResultNode create(final Object source){
        return new TestSuiteResultNode(source);
    }
    
    public static TestSuiteResultNode create(final Object source,
                                             final String testSuiteName){
        return new TestSuiteResultNode(source, testSuiteName);
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }
    
    public String getSuitName(){
        return this.suiteName;
    }

    /**
     * @return the errorCount
     */
    public int getErrorCount() {
        return errorCount;
    }

    /**
     * @return the failureCount
     */
    public int getFailureCount() {
        return failureCount;
    }

    /**
     * @return the testCaseCount
     */
    public int getTestCaseCount() {
        return testCaseCount;
    }

    /**
     * @param errorCount the errorCount to set
     */
    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    /**
     * @param failureCount the failureCount to set
     */
    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    /**
     * @param testCaseCount the testCaseCount to set
     */
    public void setTestCaseCount(int testCaseCount) {
        this.testCaseCount = testCaseCount;
    }
    
    /**
     * @return the runTime
     */
    public double getRunTime() {
        return runTime;
    }

    /**
     * @param runTime the runTime to set
     */
    public void setRunTime(double runTime) {
        this.runTime = runTime;
    }

    /**
     * @return the successCount
     */
    public int getSuccessCount() {
        return successCount;
    }

    /**
     * @param successCount the successCount to set
     */
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
}
