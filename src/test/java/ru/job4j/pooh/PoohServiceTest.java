package ru.job4j.pooh;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class PoohServiceTest {
    private final String ln = System.lineSeparator();
    private final QueueService qs = new QueueService();
    private final TopicService ts = new TopicService();

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
        assertThat(getPostResp("queue", qs).text(), is("temperature=18"));
        assertThat(getResp("queue", "", qs).status(), is(200));
        assertThat(getResp("queue", "", qs).text(), is("temperature=18"));
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
        assertThat(postTopic.text(), is("temperature=18"));
        assertThat(getTopic.status(), is(200));
        assertThat(getTopic.text(), is("temperature=18"));
    }

    @Test
    public void topicServiceWhenEmptyQueueTest() {
        Resp getTopic = getResp("topic", "/1", ts);
        assertThat(getTopic.status(), is(200));
        assertThat(getTopic.text(), is(""));
    }
}