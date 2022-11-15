package com.example.osassignment;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

public class MyConsumer extends Thread{

    private MyProducer producer;
    public Queue<Integer> buffer;
    public FileWriter writer;

    public boolean status;

    Object shared;

    public MyConsumer(Queue<Integer> buffer, FileWriter writer, Object shared) {
        this.buffer = buffer;
        this.writer = writer;
        this.shared = shared;
    }

    public MyProducer getProducer() {
        return producer;
    }

    public void setProducer(MyProducer producer) {
        this.producer = producer;
        if(producer.getConsumer() == null){
            producer.setConsumer(this);
        }
    }

    @Override
    public void run() {
        synchronized (shared){
            status = true;
            while(true){
                if(buffer.size() == 0){
                    if(producer.status == false){
                        exit();
                        break;
                    }
                    shared.notify();
                    try {
                        shared.wait();
                    } catch (InterruptedException e) {
                        System.out.println("problem in waiting.");;
                    }
                }
                Integer i = buffer.remove();
                try {
                    writer.write(String.format("\"%d\", ", i));
                } catch (IOException e) {
                    System.out.println("Problem in writing in a file");
                }
            }
        }
    }
    public void exit(){
        status = false;
        try {
            writer.write(String.format(".....\nprocess finished ...\nsample size: %d\tbuffer size: %d\tmax prime: %d\tnumber of primes: %d\n",
                    producer.sampleSize, producer.getBufferSize(), producer.max, producer.size));
            writer.close();
        } catch (IOException e) {
            System.out.println("can't terminate writing");
        }
        stop();
        producer.stop();
    }
}
