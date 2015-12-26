package framework.hessian;

import com.caucho.hessian.server.HessianServlet;

public class HelloImpl extends HessianServlet implements IHello{

	private static final long serialVersionUID = -7812327823268501789L;

	@Override
	public String sayHello() {
		return "Hello,I from HessianServlet";
	}

}
