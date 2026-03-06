package com.seven.ai;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

public class FluxDemoTest {
    public static void main(String[] args) throws InterruptedException {
        Flux<String> flux = Flux.just("Apple", "Banana", "Cherry", "Pear").delayElements(Duration.ofSeconds(1L));
        flux.map(String::toUpperCase).map(s -> s + "-1").subscribe(System.out::println);
        Thread.sleep(5000);
    }
}
