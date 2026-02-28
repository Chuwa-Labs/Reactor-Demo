package org.example;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class EventPublisher {

    public Flux<String> publishEvents() {
        return Flux.just("Event 4", "Event 5", "Event 6")
                .doOnSubscribe(subscription -> System.out.println("Event publisher subscribed at "+System.currentTimeMillis()))
                .publishOn(Schedulers.single())
                .doOnNext(event -> {
                    try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
                    System.out.println("Event publisher emits: " + event + " on Thread: " + Thread.currentThread().getName());
                })
                .doOnError(error -> System.err.println("Event publisher error: "+error))
                .doOnComplete(() -> System.out.println("Event publisher completed at "+System.currentTimeMillis()));
    }
}
