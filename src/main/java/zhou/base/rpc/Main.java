package zhou.base.rpc;

import zhou.base.rpc.op.Echo;
import zhou.base.rpc.op.RemoteEcho;
import zhou.base.rpc.support.Server;

public class Main {
	public static void main(String[] args) {
		Server server = new RPC.RPCServer();
		server.register(Echo.class, RemoteEcho.class);
		server.start();
	}
}
