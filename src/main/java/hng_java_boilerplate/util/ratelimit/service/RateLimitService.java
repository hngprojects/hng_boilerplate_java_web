package hng_java_boilerplate.util.ratelimit.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimitService {
    private static final long TIME_WINDOW = 60000; // 1 minute
    private static final int MAX_REQUESTS = 10; // max requests per time window

    private final Map<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    public boolean isAllowed(String clientIp) {
        long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(clientIp, new RequestInfo(0, currentTime));

        RequestInfo requestInfo = requestCounts.get(clientIp);
        synchronized (requestInfo) {
            if (currentTime - requestInfo.startTime > TIME_WINDOW) {
                requestInfo.startTime = currentTime;
                requestInfo.count.set(0);
            }

            if (requestInfo.count.incrementAndGet() > MAX_REQUESTS) {
                return false;
            }
        }
        return true;
    }

    private static class RequestInfo {
        AtomicInteger count;
        long startTime;

        RequestInfo(int count, long startTime) {
            this.count = new AtomicInteger(count);
            this.startTime = startTime;
        }
    }
}
