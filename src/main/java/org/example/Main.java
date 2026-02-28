package org.example;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread starts...");

        Mono.just("Task 1")
                .doOnNext(task -> {
                    System.out.println(task + " started on: " + Thread.currentThread().getName());
                    sleep(1000);
                })
                .map(task -> task + " -> Task 2")
                .doOnNext(task -> {
                    System.out.println("Task 2 started on: " + Thread.currentThread().getName());
                    sleep(1000);
                })
                .map(task -> task + " -> Task 3")
                .doOnNext(task -> {
                    System.out.println("Task 3 started on: " + Thread.currentThread().getName());
                    sleep(1000);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(result -> System.out.println("Final result: " + result));

        EventPublisher publisher = new EventPublisher();
        EventSubscriber subscriber = new EventSubscriber();
        SideSubscriber sideSubscriber = new SideSubscriber();

        // Publisher returns a Flux, subscriber subscribes to it
        subscriber.subscribeToEvents(publisher.publishEvents());
        sideSubscriber.subscribeToEvents(publisher.publishEvents());

        System.out.println("Main thread continues...");

        // Wait for tasks to finish (only for demo purpose)
        Thread.sleep(100000);
        System.out.println("Main thread ends...");
    }

    private static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}