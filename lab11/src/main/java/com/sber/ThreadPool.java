package com.sber;

public interface ThreadPool {
    void start();

    void execute(Runnable runnable);
}
