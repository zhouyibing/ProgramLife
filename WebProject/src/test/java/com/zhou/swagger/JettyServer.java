package com.zhou.swagger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by Zhou Yibing on 2015/11/10.
 */
public class JettyServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/WebProject");
        webapp.setWar("E:\\快盘\\workspace_yd\\ProgramLife\\WebProject\\target\\WebProject.war");
        //webapp.setResourceBase("E:\\快盘\\workspace_yd\\ProgramLife\\WebProject\\target\\WebProject");
        server.setHandler(webapp);

        server.start();
        server.join();
    }
}
