package zhou.j2ee.servlet;

import zhou.jetty.JettyServer;
import zhou.jetty.JettyServer.JettyServerBuilder;

public class ServletServer {

	public static void main(String[] args) {
		JettyServer JettyServer = new JettyServerBuilder(8080, "/MultiServlet", "zhou/j2ee/servlet/web.xml", "zhou/j2ee/servlet").build();
		JettyServer.start();
	}
}
