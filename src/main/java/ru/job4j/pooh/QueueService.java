package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String,
            ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        int status = 200;
        String text = "";
        String queueName = req.queueName();
        String reqMethod = req.method();
        queue.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
        if ("POST".equals(reqMethod)) {
            for (String str : req.params()) {
                text = str;
                queue.get(queueName).add(str);
            }
            status = 201;
        } else if ("GET".equals(reqMethod)) {
            text = queue.get(req.queueName()).poll();
            if (text == null) {
                text = "";
            }
        }
        return new Resp(text, status);
    }

    @Override
    public String toString() {
        return "QueueService{"
                + "queue=" + queue
                + '}';
    }
}