package com.omeraytekin.thread_safety.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class StatelessFactorizer extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) throws IOException {
        StringBuilder sb = new StringBuilder();
        resp.setContentType("text/plain");
        sb.append("Factors: \n");
        for (BigInteger factor : factors) {
            sb.append("- " + factor + "\n");
        }
        resp.getWriter().println(sb.toString());
    }

    BigInteger extractFromRequest(ServletRequest req) {
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

    BigInteger[] factor(BigInteger i) {
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
