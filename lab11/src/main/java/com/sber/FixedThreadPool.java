package com.sber;

import lombok.AllArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedThreadPool implements ThreadPool{

    private final BlockingQueue<Runnable> tasks;
    private final Thread[] threads;

    public FixedThreadPool(int nThreads){
        tasks = new LinkedBlockingQueue<>();
        threads = new Thread[nThreads];
        for (int i = 0; i < nThreads; i++){
            threads[i] = new WorkerThread(tasks);
        }
    }

    @Override
    public void start() {
        for (Thread thread : threads){
            thread.start();
        }
    }

    @Override
    public void execute(Runnable runnable){
        try{
            tasks.put(runnable);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    @AllArgsConstructor
    private static class WorkerThread extends Thread{

        private final BlockingQueue<Runnable> queue;

        @Override
        public void run(){
            while (!Thread.currentThread().isInterrupted()){
                try {
                    Runnable task = queue.take();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
