package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String,
            ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        int status = 200;
        String queueName = req.queueName();
        String reqMethod = req.method();
        String text = "";
        queue.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
        if ("POST".equals(reqMethod)) {
            for (String s : req.params()) {
                text = s;
                queue.get(queueName).add(s);
            }
            status = 201;
        } else if ("GET".equals(reqMethod)) {
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

    @Override
    public String toString() {
        return "TopicService{"
                + "queue=" + queue
                + '}';
    }
}