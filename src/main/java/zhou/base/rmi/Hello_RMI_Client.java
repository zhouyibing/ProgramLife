package zhou.base.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Hello_RMI_Client {

	public static void main(String[] args) {

		IHello hello;
		try {
			hello = (IHello) Naming.lookup("rmi://localhost:8888/hello");
			System.out.println(hello.sayHello("zhouyibing"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
