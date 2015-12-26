package zhou.base.rpc;

import zhou.base.rpc.op.Echo;

public class MainClient {
	public static void main(String[] args) {
		Echo echo = RPC.getProxy(Echo.class, "127.0.0.1", 9999);
		
		System.out.println(echo.echo("hello,hello"));
		System.out.println(echo.add(1,10));
	}
}
