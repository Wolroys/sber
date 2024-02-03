package com.sber;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

class ThreadPoolTest {

    @SneakyThrows
    @Test
    void fixedThreadPoolTest() {
        Runnable mockRunnable = mock(Runnable.class);
        FixedThreadPool fixedThreadPool = new FixedThreadPool(1);

        fixedThreadPool.start();

        fixedThreadPool.execute(mockRunnable);

        TimeUnit.SECONDS.sleep(1);

        verify(mockRunnable, times(1))
                .run();
    }

    @SneakyThrows
    @Test
    void scalableThreadPoolTest(){
        Runnable mockRunnable =  mock(Runnable.class);
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(1, 2);

        scalableThreadPool.start();

        scalableThreadPool.execute(mockRunnable);

        TimeUnit.SECONDS.sleep(1);

        verify(mockRunnable, times(1))
                .run();

        Assertions.assertEquals(1, scalableThreadPool.getCurrentThreadCount().get());
    }
}