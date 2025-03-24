package com.omeraytekin.thread_safety;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.omeraytekin.thread_safety.servlets.StatelessFactorizer;

import jakarta.servlet.GenericServlet;

public class Main {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        tomcat.setPort(8081);
        tomcat.getConnector();
        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);

        GenericServlet servlet = new StatelessFactorizer();
        String servletName = "StatelessFactorizer";
        String urlPattern = "/stateless-factorizer";

        tomcat.addServlet(contextPath, servletName, servlet);
        context.addServletMappingDecoded(urlPattern, servletName);
        tomcat.start();
        tomcat.getServer().await();
    }
}