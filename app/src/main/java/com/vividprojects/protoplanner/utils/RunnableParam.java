package com.vividprojects.protoplanner.utils;

/**
 * Created by Smile on 20.01.2018.
 */

@FunctionalInterface
public interface RunnableParam<T> {
    void run(T t);
}
