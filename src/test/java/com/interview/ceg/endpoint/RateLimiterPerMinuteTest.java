package com.interview.ceg.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RateLimiterPerMinuteTest {

    private RateLimiter rateLimiter = new RateLimiter(50L);
    private Random rnd = new Random();

    //@Test
    public void testIsAllowed()throws Exception {

//        ExecutorService executor = Executors.newFixedThreadPool(3);
//        Map<LocalDateTime, Integer> callCount = new HashMap<>();
//        Runnable api = () -> callCount.compute(LocalDateTime.now(),
//                                                (time, count) -> Optional.ofNullable(count)
//                                                                        .map(cnt -> cnt + 1)
//                                                                        .orElse(1)
//        );
//        IntStream.range(0, 3)
//                .forEach(i -> executor.submit(
//                                        () -> {
//                                            try {
//                                                for (int j = 0; j < 100; j++) {
//                                                    if (rateLimiter.isAllowed()) {
//                                                        api.run();
//                                                    }
//                                                    TimeUnit.SECONDS.sleep(rnd.nextInt(5));
//                                                }
//                                            } catch (InterruptedException e) {
//                                                log.error("Error occurred", e);
//                                            }
//                                        }
//                           )
//                );
//        executor.shutdown();
//        executor.awaitTermination(5, TimeUnit.MINUTES);
//        List<Map.Entry<LocalDateTime, Integer>> entries = callCount.entrySet()
//                .stream()
//                .sorted(Comparator.comparing(Map.Entry::getKey))
//                .collect(Collectors.toList());
//        long totalSeconds = Duration.between(entries.get(entries.size() - 1).getKey(), entries.get(0).getKey())
//                                    .getSeconds();
//        long totalRequests = entries.stream().map(Map.Entry::getValue).mapToInt(i -> i).sum();
//        log.info("Received {} requests in {} seconds", totalRequests, totalSeconds);
    }


}