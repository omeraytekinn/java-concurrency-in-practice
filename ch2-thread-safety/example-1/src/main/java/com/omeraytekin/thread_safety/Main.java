package com.omeraytekin.thread_safety;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.omeraytekin.thread_safety.servlets.CountingFactorizer;
import com.omeraytekin.thread_safety.servlets.StatelessFactorizer;
import com.omeraytekin.thread_safety.servlets.SynchronizedFactorizer;
import com.omeraytekin.thread_safety.servlets.UnsafeCachingFactorizer;
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

        GenericServlet servletCF = new CountingFactorizer();
        GenericServlet servletStlF = new StatelessFactorizer();
        GenericServlet servletSyncF = new SynchronizedFactorizer();
        GenericServlet servletUCachF = new UnsafeCachingFactorizer();
        GenericServlet servletUCntF = new UnsafeCountingFactorizer();
        String servletNameCF = "CountingFactorizer";
        String servletNameStlF = "StatelessFactorizer";
        String servletNameSyncF = "SynchronizedFactorizer";
        String servletNameUCachF = "UnsafeCachingFactorizer";
        String servletNameUCntF = "UnsafeCountingFactorizer";
        String urlPatternCF = "/counting-factorizer";
        String urlPatternStlF = "/stateless-factorizer";
        String urlPatternSync = "/synchronized-factorizer";
        String urlPatternUCachF = "/unsafe-caching-factorizer";
        String urlPatternUCntF = "/unsafe-counting-factorizer";
        tomcat.addServlet(contextPath, servletNameCF, servletCF);
        tomcat.addServlet(contextPath, servletNameStlF, servletStlF);
        tomcat.addServlet(contextPath, servletNameSyncF, servletSyncF);
        tomcat.addServlet(contextPath, servletNameUCachF, servletUCachF);
        tomcat.addServlet(contextPath, servletNameUCntF, servletUCntF);
        context.addServletMappingDecoded(urlPatternCF, servletNameCF);
        context.addServletMappingDecoded(urlPatternStlF, servletNameStlF);
        context.addServletMappingDecoded(urlPatternSync, servletNameSyncF);
        context.addServletMappingDecoded(urlPatternUCachF, servletNameUCachF);
        context.addServletMappingDecoded(urlPatternUCntF, servletNameUCntF);
        tomcat.start();
        tomcat.getServer().await();
    }
}