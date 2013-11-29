/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimuscode.core.java.testrunner.result;

import java.util.ArrayList;
import java.util.List;

public class TestMethodResult {
    private long id;
    private String name;
    private TestResult.ResultType resultType;
    private long duration;
    private long endTime;
    private List<TestFailure> failures = new ArrayList<TestFailure>();


    public TestMethodResult(long id, String name, TestResult.ResultType resultType,
                            long duration, long endTime) {
        if (id < 1) {
            throw new IllegalArgumentException("id must be > 0");
        }
        this.id = id;
        this.name = name;
        this.resultType = resultType;
        this.duration = duration;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TestFailure> getFailures(){
        return this.failures;
    }

    public TestResult.ResultType getResultType() {
        return resultType;
    }

    public long getDuration() {
        return duration;
    }

    public long getEndTime() {
        return endTime;
    }

    public TestMethodResult completed(TestResult result) {
        resultType = result.getResultType();
        duration = result.getEndTime() - result.getStartTime();
        endTime = result.getEndTime();
        return this;
    }

    public TestMethodResult addFailure(String message, String stackTrace, String exceptionType) {
        this.failures.add(new TestFailure(message, stackTrace, exceptionType));
        return this;
    }

}