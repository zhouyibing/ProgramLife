package zhou.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 一个Web应用上下文是ServletContextHandler的变种，使用标准的布局和web.xml配置Servlet、过滤器和其它特征
 *
 ***
 */
public class OneWebApp
{
   public static void main(String[] args) throws Exception
    {
	   System.setProperty("jetty.home", "E:\\jetty");
       String jetty_home =System.getProperty("jetty.home","..");
 
       Server server = new Server(8080);
 
       WebAppContext webapp = new WebAppContext();
       webapp.setContextPath("/");
       webapp.setWar(jetty_home+"/webapps/parser.war");
       server.setHandler(webapp);
 
       server.start();
       server.join();
    }
}