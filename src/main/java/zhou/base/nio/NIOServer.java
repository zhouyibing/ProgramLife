package zhou.base.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 
使用NIO中非阻塞I/O编写服务器处理程序，大体上可以分为下面三个步骤：
1. 向Selector对象注册感兴趣的事件 
2. 从Selector中获取感兴趣的事件 
3. 根据不同的事件进行相应的处理
 ***
 */
public class NIOServer {
	
	//通道管理器
	private Selector selector;
	
	/** 
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作 
     * @param port  绑定的端口号 
     * @throws IOException 
     */  
	public void initServer(int port) throws IOException{

		//获得一个ServerSocket通道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		//设置通道为非阻塞
		serverSocketChannel.configureBlocking(false);
		//将改通道对应的ServerSocket绑定到port端口
		serverSocketChannel.bind(new InetSocketAddress(port));
		//获得一个通道管理器
		selector = Selector.open();
		//将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，  
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。 
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	  /** 
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理 
     * @throws IOException 
     */  
	public void listen() throws IOException{
		System.out.println("服务端启动成功！"); 
		// 轮询访问selector 
		while(true){
			//当注册的事件到达时，方法返回；否则,该方法会一直阻塞  
			selector.select(100L);
			Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
			while(ite.hasNext()){
				SelectionKey key = ite.next();
				ite.remove();
				if(key.isAcceptable()){
					ServerSocketChannel server = (ServerSocketChannel) key  
	                            .channel();  
                    // 获得和客户端连接的通道  
                    SocketChannel channel = server.accept();  
                    // 设置成非阻塞  
                    channel.configureBlocking(false);  
  
                    //在这里可以给客户端发送信息哦  
                    channel.write(ByteBuffer.wrap(new String("send a message from server").getBytes()));  
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。  
                    channel.register(this.selector, SelectionKey.OP_READ);  
                      
                    // 获得了可读的事件 	
				}else if(key.isReadable()){
					read(key);
				}
			}
		}
	}

	/** 
     * 处理读取客户端发来的信息 的事件 
     * @param key 
     * @throws IOException  
     */ 
	private void read(SelectionKey key) throws IOException {
		  // 服务器可读取消息:得到事件发生的Socket通道  
        SocketChannel channel = (SocketChannel) key.channel();  
        // 创建读取的缓冲区  
        ByteBuffer buffer = ByteBuffer.allocate(100);  
        channel.read(buffer);  
        byte[] data = buffer.array();  
        String msg = new String(data).trim();  
        System.out.println("服务端收到信息："+msg);  
        //ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());  
        //channel.write(outBuffer);// 将消息回送给客户端 
	}
	
	public static void main(String[] args) throws IOException {  
        NIOServer server = new NIOServer();  
        server.initServer(8000);  
        server.listen();  
    }  
}