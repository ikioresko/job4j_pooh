package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        int status = 200;
        String text = req.text();
        String queueName = req.queueName();
        String reqMethod = req.method();
        queue.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
        if (reqMethod.equals("POST")) {
            queue.get(queueName).add(text);
            status = 201;
        } else if (reqMethod.equals("GET")) {
            ConcurrentLinkedQueue<String> clqCopy
                    = new ConcurrentLinkedQueue<>(new ConcurrentHashMap<>(queue).get(queueName));
            text = clqCopy.poll();
            queue.get(queueName).poll();
            if (text == null) {
                text = "";
            }
        }
        return new Resp(text, status);
    }
}