package com.omeraytekin.thread_safety.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class SynchronizedFactorizer extends GenericServlet {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();

    @Override
    public synchronized void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get())) {
            encodeIntoResponse(resp, lastFactors.get());
        } else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(resp, factors);
        }
    }

    public synchronized BigInteger getLastNumber() {
        return lastNumber.get();
    }

    public synchronized BigInteger[] getLastFactors() {
        return lastFactors.get();
    }

    private synchronized void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) throws IOException {
        StringBuilder sb = new StringBuilder();
        resp.setContentType("text/plain");
        sb.append("Factors: \n");
        for (BigInteger factor : factors) {
            sb.append(", " + factor + "\n");
        }
        resp.getWriter().println(sb.toString());
    }

    private synchronized BigInteger extractFromRequest(ServletRequest req) {
        String val = req.getParameter("value");
        if (val == null) {
            return BigInteger.ZERO;
        }
        try {
            return new BigInteger(val);
        } catch (NumberFormatException e) {
            return BigInteger.ZERO;
        }
    }

    public synchronized BigInteger[] factor(BigInteger i) {
        if (i.compareTo(BigInteger.ONE) <= 0) {
            return new BigInteger[] { i };
        }

        BigInteger divisor = BigInteger.TWO;
        BigInteger num = i;
        List<BigInteger> factors = new ArrayList<>();

        while (num.compareTo(BigInteger.ONE) > 0) {
            while (num.mod(divisor).equals(BigInteger.ZERO)) {
                factors.add(divisor);
                num = num.divide(divisor);
            }
            divisor = divisor.add(BigInteger.ONE);
        }

        return factors.toArray(new BigInteger[0]);
    }
}
