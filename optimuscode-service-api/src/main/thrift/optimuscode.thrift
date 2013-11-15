namespace  java com.optimuscode.thrift.api

typedef i64 long
typedef i32 int
typedef list<string> ErrorList
typedef list<string> TestMsgList

struct Session {
    1: string uuid;
    2: string timestamp;

}

struct SourceUnit {
    1: string className;
    2: string sourceCode;
    3: string testClassName;
    4: string testSourceCode;
    5: long duration;
}

struct CompilerResult {
    1: bool passed;
    2: ErrorList errors;
}

struct TestResult {
    1: int testCaseCount;
    2: int errorCount;
    3: int failureCount;
    4: int successCount;
    5: double runTime;
    6: TestMsgList testMessages;
}

service RpcCompileNTestService {
    string echo(1: string msg);
    CompilerResult compile(1: Session session, 2: SourceUnit unit);
    TestResult runTest(1: Session session, 2: SourceUnit unit);
}