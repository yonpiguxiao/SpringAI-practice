package com.seven.ai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Date;

@RequestMapping("/sse")
@RestController
public class SseController {
    @RequestMapping("/data")
    public void data(HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        for(int i = 0; i < 20; i++) {
            String s = "data: " + new Date() + "\n\n";
            System.out.println(s);
            writer.write(s);
            writer.flush();
            Thread.sleep(1000L);
        }
    }

    @RequestMapping("/retry")
    public void retry(HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String s = "retry: 2000\n";
        s += "data: " + new Date() + "\n\n";
        System.out.println(s);
        writer.write(s);
        writer.flush();
    }


    @RequestMapping("/event")
    public void event(HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        for(int i = 0; i < 10; i++) {
            String s = "event: foo\n";
            s += "data: " + new Date() + "\n\n";
            System.out.println(s);
            writer.write(s);
            writer.flush();
            Thread.sleep(1000L);
        }
        writer.write("event: end\ndata: EOF\n\n");
        writer.flush();
    }

    @RequestMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream() {
        return Flux.interval(Duration.ofSeconds(1)).map(s->new Date().toString());
    }
}
