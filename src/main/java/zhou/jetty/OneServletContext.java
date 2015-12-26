package zhou.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * ServletContextHandler是一个支持标准Servlet的专用ContextHandler。
 * 下面的代码来自于OneServletContext，显示三个HelloworldServlet实例注册到一个ServletContextHandler上。
 *
 ***
 */
public class OneServletContext
{
   public static void main(String[] args) throws Exception
    {
       Server server = new Server(8080);
 
       ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
       context.setContextPath("/");
       server.setHandler(context);
 
       context.addServlet(new ServletHolder(new HelloServlet()),"/*");
       context.addServlet(new ServletHolder(new HelloServlet("BuongiornoMondo")),"/it/*");
       context.addServlet(new ServletHolder(new HelloServlet("Bonjour leMonde")),"/fr/*");
 
       server.start();
       server.join();
    }
}