package com.optimuscode.thrift.client;

import com.optimuscode.thrift.api.CompilerResult;
import com.optimuscode.thrift.api.Session;
import com.optimuscode.thrift.api.SourceUnit;
import com.optimuscode.thrift.api.TestResult;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/6/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OptimusPrimeRpcClientTest {
    String sessionId;

    @Before
    public void setUp() throws Exception {
        this.sessionId = generateUUID();

    }

    @Test
    public void testCompile() throws Exception{
        String classSource = "optimuscode-service-client/src/test/resources/TestSourceFile.txt";
        final String testSource = "optimuscode-api/src/test/resources/TestSourceFileTest.txt";
        String className = "TestSourceFile";
        String testClassName = "TestSourceFileTest";
        String fileContents1 = FileUtils.readFileToString(new File(classSource));
        String fileContents2 = FileUtils.readFileToString(new File(testSource));
        Session session = new Session.Builder().
                              uuid(this.sessionId).
                              timestamp(
                                String.valueOf(System.currentTimeMillis())
                              ).build();
        SourceUnit sourceUnit = new SourceUnit.Builder().
                                    sourceCode(fileContents1).
                                    className(className).
                                    testClassName(testClassName).
                                    testSourceCode(fileContents2).
                                    build();
        Client client = new OptimusPrimeRpcClient();
        CompilerResult compileResult = client.compile(session, sourceUnit);
        assertTrue(compileResult.getPassed());
    }

    protected static String generateUUID(){
        String uuid = UUID.randomUUID().toString();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }

    @Test
    public void testRunTest() throws Exception{
        String classSource = "optimuscode-service-client/src/test/resources/TestSourceFile.txt";
        String testSource = "optimuscode-service-client/src/test/resources/TestSourceFileTest.txt";
        String className = "TestSourceFile";
        String testClassName = "TestSourceFileTest";
        String fileContents1 = FileUtils.readFileToString(new File(classSource));
        String fileContents2 = FileUtils.readFileToString(new File(testSource));
        Session session = new Session.Builder().
                uuid(this.sessionId).
                timestamp(
                        String.valueOf(System.currentTimeMillis())
                ).build();
        SourceUnit sourceUnit = new SourceUnit.Builder().
                sourceCode(fileContents1).
                className(className).
                testClassName(testClassName).
                testSourceCode(fileContents2).
                build();
        Client client = new OptimusPrimeRpcClient();
        CompilerResult compileResult = client.compile(session, sourceUnit);
        assertTrue(compileResult.getPassed());
        TestResult testResult = client.runTest(session, sourceUnit);
        assertTrue(testResult.getSuccessCount() == 1);
        assertTrue(testResult.getFailureCount() == 1);
        assertTrue(testResult.getErrorCount() == 0);
        assertTrue(testResult.getRunTime() > 0);
    }
}
