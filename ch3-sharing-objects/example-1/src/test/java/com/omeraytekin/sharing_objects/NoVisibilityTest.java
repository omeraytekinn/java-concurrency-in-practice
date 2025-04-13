package com.omeraytekin.sharing_objects;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NoVisibilityTest {

    @Test
    void testNoVisibilityBehavior() throws InterruptedException {
        int iterations = 100;
        int zeroCount = 0;
        int fortyTwoCount = 0;
        int timeoutCount = 0;

        for (int i = 0; i < iterations; i++) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            NoVisibility.ready = false;
            NoVisibility.number = 0;

            Future<?> future = executor.submit(new NoVisibility.ReaderThread());

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }

            NoVisibility.number = 42;
            NoVisibility.ready = true;

            try {
                future.get(1, TimeUnit.SECONDS);
                if (NoVisibility.number == 42) {
                    fortyTwoCount++;
                } else {
                    zeroCount++;
                }
            } catch (TimeoutException e) {
                timeoutCount++;
                future.cancel(true);
            } catch (ExecutionException e) {
                fail("Unexpected exception: " + e.getMessage());
            }

            executor.shutdownNow();
        }

        assertTrue(zeroCount > 0 || timeoutCount > 0, "At least one failure due to visibility expected");
        assertTrue(fortyTwoCount > 0, "At least one successful read of 42 expected");
    }
}
