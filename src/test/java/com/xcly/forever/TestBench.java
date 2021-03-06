package com.xcly.forever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TestBench {

    private final static Logger logger = LoggerFactory.getLogger(TestBench.class);

    public static CountDownLatch countDownLatch = new CountDownLatch(10000);

    public static void main(String[] args) {
        Random random = new Random();
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i <20000; i++) {
            new Thread(new MyThread(logger, restTemplate, list.get(random.nextInt(3)))).start();
            countDownLatch.countDown();
        }
    }


    public static class MyThread implements Runnable {

        RestTemplate restTemplate;
        Integer count;
        Logger logger;

        public MyThread(Logger logger, RestTemplate restTemplate, Integer count) {
            this.restTemplate = restTemplate;
            this.count = count;
            this.logger = logger;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                restTemplate.getForObject("http://192.168.1.153:10013/start?mid=316952840381661184&count=1", Object.class);
            } catch (Exception e) {
                logger.error("<<==failed");
            }
        }
    }
}
