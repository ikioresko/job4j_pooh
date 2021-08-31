package ru.job4j.pooh;

import java.util.List;

public class Req {
    private final String method;
    private final String mode;
    private final String queueName;
    private final String text;

    private Req(String method, String mode, String queueName, String text) {
        this.method = method;
        this.mode = mode;
        this.queueName = queueName;
        this.text = text;
    }

    public static Req of(String content) {
        List<String> list = List.of(content.split(System.lineSeparator()));
        String sysInfo = list.get(0);
        List<String> keywords = List.of(sysInfo
                .substring(0, sysInfo.indexOf("HTTP"))
                .replace(" ", "")
                .split("/"));
        String text = list.get(list.size() - 1);
        return new Req(keywords.get(0), keywords.get(1), keywords.get(2), text);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String queueName() {
        return queueName;
    }

    public String text() {
        return text;
    }

    @Override
    public String toString() {
        return "Req{"
                + "method='" + method + '\''
                + ", mode='" + mode + '\''
                + ", themeName='" + queueName + '\''
                + ", text='" + text + '\''
                + '}';
    }
}
