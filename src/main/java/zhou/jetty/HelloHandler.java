package zhou.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
/**
 * This gives three styles of handler:
Coordinating Handlers - Handlers that route requests to other handlers (HandlerCollection, ContextHandlerCollection) 
Filtering Handlers - Handlers that augment a request and pass it on to other handlers (HandlerWrapper, ContextHandler, SessionHandler) 
Generating Handlers - Handlers that produce content (ResourceHandler and ServletHandler)
 */
public class HelloHandler extends AbstractHandler{

	@Override
	public void handle(String arg0, Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().print("<h1>HelloWorld</h1>");
	}
}
