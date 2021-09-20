package ru.job4j.pooh;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PoohServiceTest {
    private final String ln = System.lineSeparator();
    private final QueueService qs = new QueueService();
    private final TopicService ts = new TopicService();

    @Test
    public void whenPostThenGetTopic() {
        var topicService = new TopicService();
        Map<String, String> params = new HashMap<>();
        params.put("temperature", "18");
        topicService.process(
                new Req("POST", "topic", "weather", params)
        );
        var result = topicService.process(
                new Req("GET", "topic", "weather", params)
        );
        assertThat(result.text(), is("18"));
    }

    @Test
    public void whenPostThenGetQueue() {
        var queueService = new QueueService();
        Map<String, String> params = new HashMap<>();
        params.put("temperature", "18");
        queueService.process(
                new Req("POST", "queue", "weather", params)
        );
        var result = queueService.process(
                new Req("GET", "queue", "weather", new HashMap<>())
        );
        assertThat(result.text(), is("18"));
    }

    private Resp getPostResp(String mode, Service service) {
        return service.process(Req.of("POST /" + mode + "/weather HTTP/1.1" + ln
                + "Host: localhost:9000" + ln
                + "User-Agent: curl/7.75.0" + ln
                + "Accept: */*" + ln
                + "Content-Length: 7" + ln
                + "Content-Type: application/x-www-form-urlencoded" + ln
                + "temperature=18"));
    }

    private Resp getResp(String mode, String id, Service service) {
        return service.process(Req.of("GET /" + mode + "/weather" + id + " HTTP/1.1" + ln
                + "Host: localhost:9000" + ln
                + "User-Agent: curl/7.75.0" + ln
                + "Accept: */*" + ln
                + "Content-Length: 7" + ln
                + "Content-Type: application/x-www-form-urlencoded"
        ));
    }

    @Test
    public void queueServiceTest() {
        assertThat(getPostResp("queue", qs).status(), is(201));
        assertThat(getPostResp("queue", qs).text(), is("18"));
        assertThat(getResp("queue", "", qs).status(), is(200));
        assertThat(getResp("queue", "", qs).text(), is("18"));
    }

    @Test
    public void queueServiceWhenEmptyQueueTest() {
        assertThat(getResp("queue", "", qs).status(), is(200));
        assertThat(getResp("queue", "", qs).text(), is(""));
    }

    @Test
    public void topicServiceTest() {
        Resp postTopic = getPostResp("topic", ts);
        Resp getTopic = getResp("topic", "/1", ts);
        assertThat(postTopic.status(), is(201));
        assertThat(postTopic.text(), is("18"));
        assertThat(getTopic.status(), is(200));
        assertThat(getTopic.text(), is("18"));
    }

    @Test
    public void topicServiceWhenEmptyQueueTest() {
        Resp getTopic = getResp("topic", "/1", ts);
        assertThat(getTopic.status(), is(200));
        assertThat(getTopic.text(), is(""));
    }

    @Test
    public void whenPostMethod() {
        var content = "POST /topic/weather HTTP/1.1" + ln
                + "Host: localhost:9000" + ln
                + "User-Agent: curl/7.75.0" + ln
                + "Accept: */*" + ln
                + "Content-Length: 14" + ln
                + "Content-Type: application/x-www-form-urlencoded" + ln
                + ln
                + "text=13";

        var req = Req.of(content);
        assertThat(req.method(), is("POST"));
        assertThat(req.mode(), is("topic"));
        assertThat(req.queueName(), is("weather"));
        assertThat(req.params("text"), is("13"));
    }

    @Test
    public void whenGetMethod() {
        var content = "GET /queue/weather HTTP/1.1" + ln
                + "Host: localhost:9000" + ln
                + "User-Agent: curl/7.67.0" + ln
                + "Accept: */*" + ln
                + ln
                + "userId=1";
        var req = Req.of(content);
        assertThat(req.method(), is("GET"));
        assertThat(req.mode(), is("queue"));
        assertThat(req.queueName(), is("weather"));
        assertThat(req.params("userId"), is("1"));
    }
}