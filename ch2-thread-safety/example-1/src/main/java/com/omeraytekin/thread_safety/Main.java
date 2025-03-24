package com.omeraytekin.thread_safety;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.omeraytekin.thread_safety.servlets.StatelessFactorizer;
import com.omeraytekin.thread_safety.servlets.UnsafeCountingFactorizer;

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

        GenericServlet servletSF = new StatelessFactorizer();
        GenericServlet servletUCF = new UnsafeCountingFactorizer();
        String servletNameSF = "StatelessFactorizer";
        String urlPatternSF = "/stateless-factorizer";
        String servletNameUCF = "UnsafeCountingFactorizer";
        String urlPatternUCF = "/unsafe-counting-factorizer";
        tomcat.addServlet(contextPath, servletNameSF, servletSF);
        tomcat.addServlet(contextPath, servletNameUCF, servletUCF);
        context.addServletMappingDecoded(urlPatternSF, servletNameSF);
        context.addServletMappingDecoded(urlPatternUCF, servletNameUCF);
        tomcat.start();
        tomcat.getServer().await();
    }
}