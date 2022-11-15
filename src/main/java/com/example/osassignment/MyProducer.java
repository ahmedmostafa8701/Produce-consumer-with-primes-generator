package com.example.osassignment;

import java.util.Queue;

public class MyProducer extends Thread{

    private Integer bufferSize;
    private MyConsumer consumer;
    public Queue<Integer> buffer;
    Object shared;
    public Integer sampleSize;
    public Integer max;
    public Integer size;
    private Integer i;

    private long startTime;

    boolean status;

    public MyProducer(Queue<Integer> buffer, Integer bufferSize, Integer sampleSize, Object shared) {
        this.buffer = buffer;
        this.sampleSize = sampleSize;
        i = 2;
        max = -1;
        size = 0;
        this.shared = shared;
        this.bufferSize = bufferSize;
    }

    public MyConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(MyConsumer consumer) {
        this.consumer = consumer;
        if(consumer.getProducer() == null){
            consumer.setProducer(this);
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        synchronized (shared){
            status = true;
            while (i <= sampleSize) {
                if (buffer.size() >= bufferSize) {
                    shared.notify();
                    try {
                        shared.wait();
                    } catch (InterruptedException e) {
                        System.out.println("problem in waiting.");
                    }
                }
                if (isPrime(i)) {
                    buffer.add(i);
                    size++;
                    max = Math.max(max, i);
                    shared.notify();
                }
                i++;
            }
            status = false;
        }
    }
    private boolean isPrime(Integer num){
        for(int j = 2; j * j <= num; j++) {
            if(num % j == 0){
                return false;
            }
        }
        return true;
    }
}
