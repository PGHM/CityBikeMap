package com.pghm.citybikes.network;

public interface Callback<T> {
    void callback(T result);
}
