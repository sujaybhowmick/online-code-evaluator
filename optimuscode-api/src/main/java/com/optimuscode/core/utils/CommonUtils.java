package com.optimuscode.core.utils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class CommonUtils {

    private CommonUtils(){}

    public static String generateUUID(){
        String uuid = UUID.randomUUID().toString();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }
}
