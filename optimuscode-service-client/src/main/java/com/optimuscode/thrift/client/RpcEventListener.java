package com.optimuscode.thrift.client;

import com.twitter.util.Function;
import scala.runtime.BoxedUnit;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/7/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class RpcEventListener<T, R> extends Function<T, R> {
    private T value;
    private Throwable throwable;


    public T getValue(){
        return this.value;
    }

    public Throwable getThrowable(){
        return this.throwable;
    }

    @Override
    public R apply(T value) {
        this.value = value;
        return (R)BoxedUnit.UNIT;
    }
}
