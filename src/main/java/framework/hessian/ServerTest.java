package framework.hessian;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServerTest {

	/**
	 * 利用jetty服务器，设置应用上下文
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/hello");
	    webapp.setDescriptor("src/main/java/framework/hessian/web.xml");
	    webapp.setResourceBase("src/main/java/framework/hessian");
		server.setHandler(webapp);
		server.start();
		server.join();
	}

}
