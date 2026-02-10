package filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitAwareFilter implements Filter {

    private final int maxRetries;
    private final long defaultBackoffMillis;
    private final long maxWaitMillis;

    private final AtomicInteger retryCounter = new AtomicInteger();
    private final AtomicInteger rateLimitHits = new AtomicInteger();

    public RateLimitAwareFilter(int maxRetries,
                                long defaultBackoffMillis,
                                long maxWaitMillis) {
        this.maxRetries = maxRetries;
        this.defaultBackoffMillis = defaultBackoffMillis;
        this.maxWaitMillis = maxWaitMillis;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        int attempt = 0;
        Response response;

        while (true) {
            response = ctx.next(requestSpec, responseSpec);

            // GitHub returns 403 when rate limit is exceeded
            if (response.statusCode() != 429 && response.statusCode() != 403) {
                return response;
            }

            rateLimitHits.incrementAndGet();
            attempt++;

            if (attempt > maxRetries) {
                throw new RuntimeException(
                        "Rate limit exceeded. Max retries reached: " + maxRetries
                );
            }

            long waitMillis = calculateWaitTime(response, attempt);

            if (waitMillis > maxWaitMillis) {
                throw new RuntimeException(
                        "Rate limit reset too far in future (" + waitMillis + " ms). Aborting."
                );
            }

            retryCounter.incrementAndGet();

            logRateLimitEvent(response, attempt, waitMillis);

            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry interrupted", e);
            }
        }
    }

    private long calculateWaitTime(Response response, int attempt) {

        // 1️⃣ Retry-After (seconds)
        String retryAfter = response.getHeader("Retry-After");
        if (retryAfter != null) {
            return withJitter(Long.parseLong(retryAfter) * 1000);
        }

        // 2️⃣ X-RateLimit-Reset (epoch seconds)
        String reset = response.getHeader("X-RateLimit-Reset");
        if (reset != null) {
            long resetEpoch = Long.parseLong(reset);
            long now = Instant.now().getEpochSecond();
            long waitMillis = Math.max((resetEpoch - now) * 1000, 0);
            return withJitter(waitMillis);
        }

        // 3️⃣ Fallback: exponential backoff
        return withJitter(defaultBackoffMillis * attempt);
    }

    private long withJitter(long baseDelay) {
        long jitter = (long) (Math.random() * 1000); // up to 1s
        return baseDelay + jitter;
    }

    private void logRateLimitEvent(Response response, int attempt, long waitMillis) {
        System.out.println(
                "[RateLimitFilter] " +
                "Attempt=" + attempt +
                ", Status=" + response.statusCode() +
                ", Remaining=" + response.getHeader("X-RateLimit-Remaining") +
                ", Wait(ms)=" + waitMillis
        );
    }

    // Optional: expose metrics
    public int getRetryCount() {
        return retryCounter.get();
    }

    public int getRateLimitHits() {
        return rateLimitHits.get();
    }
}
