package zhou.jetty;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
//上下文处理器集合使用最长请求URI前缀选择指定的上下文。下面的示例在单个Jetty服务上组合前面两个示例。
public class ManyContexts
{
   public static void main(String[] args) throws Exception
    {
       Server server = new Server(8080);
 
       ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.SESSIONS);
       context0.setContextPath("/ctx0");
       context0.addServlet(new ServletHolder(new HelloServlet()),"/*");
       context0.addServlet(new ServletHolder(new HelloServlet("BuongiornoMondo")),"/it/*");
       context0.addServlet(new ServletHolder(new HelloServlet("Bonjour leMonde")),"/fr/*");
 
       WebAppContext webapp = new WebAppContext();
       webapp.setContextPath("/ctx1");
       webapp.setWar("E:\\jetty\\webapps\\test.war");
 
       ContextHandlerCollection contexts = new ContextHandlerCollection();
       contexts.setHandlers(new Handler[] { context0, webapp });
 
       server.setHandler(contexts);
 
       server.start();
       server.join();
    }
}
