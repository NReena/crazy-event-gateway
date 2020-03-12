package com.interview.ceg.endpoint;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RateLimiter {

    private ConcurrentHashMap<LocalDateTime, Long> permitMap = new ConcurrentHashMap<>();

    private Long maxRequestPerHour;

    public RateLimiter(@Value("${ratelimiter.maxrequestsperhour}") Long maxRequestPerHour) {
        this.maxRequestPerHour = maxRequestPerHour;
    }

    public boolean isAllowed() {
        log.info("Permit map: {}", permitMap);
        LocalDateTime currentMin = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Long reqCount = permitMap.compute(currentMin, (ts, requestCount) -> {
            if (requestCount == null) {
                return 1L;
            }
            return requestCount+1;
        });
        long totalReqCountLastHour = IntStream.range(1, 59)
                .mapToObj(currentMin::minusMinutes)
                .filter(minute -> permitMap.containsKey(minute))
                .map(minute -> permitMap.get(minute))
                .flatMapToLong(LongStream::of)
                .sum() + reqCount;
        return totalReqCountLastHour <= maxRequestPerHour;
    }

    @Scheduled(initialDelay = 125*60*1000, fixedRate = 60*60*1000) // it runs per hour to remove the old keys
    public void shrinkMap() {
        log.info("Running map shrink job");
        LocalDateTime currentMin = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        IntStream.range(1, 59).mapToObj(currentMin::minusMinutes)
                .forEach(minute -> permitMap.remove(minute));
        log.info("Removed records last 60 minutes from : {}", currentMin);
    }
}
