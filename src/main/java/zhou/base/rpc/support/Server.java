package zhou.base.rpc.support;

import zhou.base.rpc.protocol.Invocation;

/**
 * 定义服务器类，主要是实现服务器的启动停止，注册。处理执行请求
 * 
 * 服务器提供了start和stop方法。
 * 使用register注册一个接口和对应的实现类。
 * call方法用于执行Invocation指定的接口的方法名。
 * isRunning返回了服务器的状态，
 * getPort（）则返回了服务器使用的端口。
 ***
 */
public interface Server {

	public void stop();
	public void start();
	public void register(Class interfaceDefiner,Class impl);
	public void call(Invocation invo);
	public boolean isRunning();
	public int getPort();
}
