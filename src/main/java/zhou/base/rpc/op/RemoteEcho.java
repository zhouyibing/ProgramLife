package zhou.base.rpc.op;

public class RemoteEcho implements Echo{
	public String echo(String echo) {
		return "from remote:"+echo;
	}

	@Override
	public String add(int a, int b) {
		return "from remote:the add result is "+(a+b);
	}
}
