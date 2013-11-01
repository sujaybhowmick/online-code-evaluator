namespace  java com.optimuscode.thrift

typedef i64 long
typedef i32 int

struct SourceUnit {
    1: string className;
    2: string sourceCode;
}

struct Session {
    1: string sessionId;
    2: SourceUnit sourceCode;
    3: string testClassName;
    4: long duration;
}