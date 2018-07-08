package com.vividprojects.protoplanner.utils;

public interface DataQuery<T,R> {
    public R find(T t);
}
