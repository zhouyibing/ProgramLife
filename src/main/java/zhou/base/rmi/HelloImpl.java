package zhou.base.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements IHello{

	private static final long serialVersionUID = -7277976498530274514L;

	protected HelloImpl() throws RemoteException {
		super();
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		return "Hello "+name;
	}
	
	public static void main(String[] args) {
		try {
			IHello hello = new HelloImpl();
			LocateRegistry.createRegistry(8888);//加上此程序，就可以不要在控制台上开启RMI的注册程序，1099是RMI服务监视的默认端口
			Naming.bind("rmi://localhost:8888/hello", hello);
			System.out.println("Ready......");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
