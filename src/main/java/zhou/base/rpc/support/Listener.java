package zhou.base.rpc.support;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import zhou.base.rpc.protocol.Invocation;

/**
 *监听/控制客户端与服务端之间的通信，为客户端和服务端建立连接
 ***
 */
public class Listener extends Thread{
	
	private ServerSocket socket;
	private Server server;
	
	public Listener(Server server){
		this.server = server;
	}
	
	@Override
	public void run() {
		System.out.println("启动服务器中，打开端口" + server.getPort());  
		try {
			socket = new ServerSocket(server.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(server.isRunning()){
			try {
				Socket client = socket.accept();
				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				Invocation invo = (Invocation)ois.readObject();
				server.call(invo);//handling the data send from client
			    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
			    oos.writeObject(invo);//return the  after handled  data to client
			    oos.close();
			    ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
			//close the socket
			try {
			if(socket!=null&&!socket.isClosed())
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
}
