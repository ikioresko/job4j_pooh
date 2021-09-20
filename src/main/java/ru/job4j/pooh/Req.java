package ru.job4j.pooh;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Req {
    private final String method;
    private final String mode;
    private final String queue;
    private final Map<String, String> params;

    public Req(String method, String mode, String queue, Map<String, String> params) {
        this.method = method;
        this.mode = mode;
        this.queue = queue;
        this.params = params;
    }

    public static Req of(String content) {
        List<String> list = List.of(content.split(System.lineSeparator()));
        String sysInfo = list.get(0);
        List<String> keywords = List.of(sysInfo
                .substring(0, sysInfo.indexOf("HTTP"))
                .replace(" ", "")
                .split("/"));
        List<String> spl = List.of(list.get(list.size() - 1).split("="));
        Map<String, String> params = new HashMap<>();
        String val2 = spl.size() == 2 ? spl.get(1) : "";
        params.put(spl.get(0), val2);
        return new Req(keywords.get(0), keywords.get(1), keywords.get(2), params);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String queueName() {
        return queue;
    }

    public Collection<String> params() {
        return params.values();
    }

    public String params(String key) {
        return params.get(key);
    }

    @Override
    public String toString() {
        return "Req{"
                + "method='" + method + '\''
                + ", mode='" + mode + '\''
                + ", themeName='" + queue + '\''
                + ", text='" + params + '\''
                + '}';
    }
}
