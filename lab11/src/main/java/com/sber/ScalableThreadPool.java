package com.sber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


@Getter
@Setter
public class ScalableThreadPool implements ThreadPool {

    private final BlockingQueue<Runnable> tasks;
    private final Thread[] threads;
    private final int minThreads;
    private final int maxThreads;
    private final AtomicInteger currentThreadCount;

    public ScalableThreadPool(int minThreads, int maxThreads){
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        tasks = new LinkedBlockingQueue<>();
        threads = new Thread[maxThreads];
        currentThreadCount = new AtomicInteger(minThreads);

        for (int i = 0; i <= minThreads; i++){
            threads[i] = new WorkerThread(tasks, currentThreadCount, minThreads);
        }
    }
    @Override
    public void start() {
        for (Thread thread : threads)
            thread.start();
    }

    @Override
    public void execute(Runnable runnable){
        if (tasks.isEmpty() && currentThreadCount.get() < maxThreads){
            threads[currentThreadCount.getAndIncrement()] = new WorkerThread(tasks, currentThreadCount, minThreads);

            threads[currentThreadCount.get() - 1].start();
        }
        try {
            tasks.put(runnable);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AllArgsConstructor
    private static class WorkerThread extends Thread{

        private final BlockingQueue<Runnable> queue;
        private final AtomicInteger currentThreadCount;
        private final int min;

        @Override
        public void run(){
            while (!Thread.currentThread().isInterrupted())
                try{
                    Runnable runnable = queue.take();
                    runnable.run();
                    if (queue.isEmpty() && currentThreadCount.get() > min){
                        currentThreadCount.decrementAndGet();
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
        }
    }
}
