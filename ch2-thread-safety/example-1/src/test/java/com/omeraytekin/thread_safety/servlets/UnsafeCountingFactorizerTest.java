package com.omeraytekin.thread_safety.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class UnsafeCountingFactorizerTest {
    private UnsafeCountingFactorizer factorizer;
    private ServletRequest mockRequest;
    private ServletResponse mockResponse;
    private StringWriter responseWriter;

    @BeforeEach
    public void setUp() throws IOException {
        factorizer = new UnsafeCountingFactorizer();
        mockRequest = mock(ServletRequest.class);
        mockResponse = mock(ServletResponse.class);
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(mockResponse.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testCountRaceCondition() throws Exception {
        int threadCount = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    latch.await();
                    factorizer.service(mockRequest, mockResponse);
                } catch (InterruptedException | ServletException | IOException e) {
                    e.printStackTrace();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        System.out.println("Final Count: " + factorizer.getCount());
        assertNotEquals(10000, factorizer.getCount(), "Race condition expected, count should NOT be exactly 10000");
    }
}
