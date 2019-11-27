package org.grpc.client;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

class ClientTest {

    private final Logger logger = Logger.getLogger(ClientTest.class.getName());

    private List<String> servers = Arrays.asList(
            "google.com:80", "4pda.ru:80", "dp.informator.ua:80", "gorod.dp.ua:80", "bigmir.net:80", "i.ua:80",
            "stackoverflow.com:80", "ubuntu.com:80", "microsoft.com:80", "getfedora.org:80", "spring-projects.ru:80",
            "youtube.com:80", "java.com:80", "jetbrains.com:80", "bash.im:80", "apple.com:80", "github.com:80",
            "medium.com:80", "about.gitlab.com:80", "bitbucket.org:80");

    @Test
    void singleThreadBlockingGreet() {
        for (String server : servers) {
            Client client = new Client(server);
            client.blockingGreet(server);
        }
    }

    @Test
    void SingleThreadAsyncGreet() {
        for (String server : servers) {
            Client client = new Client(server);
            client.asyncGreet(server);
        }
    }

    @Test
    void MultiThreadBlockingGreet() {
        for (final String server : servers) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Client client = new Client(server);
                    client.blockingGreet(server);
                }
            };
            logger.info("Thread - " + thread.getName() + " start");
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Thread - " + thread.getName() + " stop");
        }
    }

    @Test
    void MultiThreadNonBlockingGreet() {

        List<Thread> threadList = new ArrayList<Thread>();

        for (final String server : servers) {
            threadList.add(
                    new Thread() {
                        @Override
                        public void run() {
                            Client client = new Client(server);
                            client.blockingGreet(server);
                        }
                    });
        }

        for (Thread thread : threadList) {
            logger.info("Thread - " + thread.getName() + " start");
            thread.start();
        }
    }
}