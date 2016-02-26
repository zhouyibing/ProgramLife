package framework.mina.chat.client;
import framework.mina.chat.demo.CodecFactory;
import framework.mina.chat.server.manage.Connection;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created by Zhou Yibing on 2016/2/26.
 */
public class ChatClient implements IChatClient{

    private SocketConnector socketConnector;
    private Connection connection;
    public ChatClient(){
        socketConnector = new NioSocketConnector();
    }

    @Override
    public void start(){
        //1.设置过滤器
        DefaultIoFilterChainBuilder filterChainBuilder = socketConnector.getFilterChain();
        filterChainBuilder.addLast("codec",new ProtocolCodecFilter(new CodecFactory()));

        //2.设置handler
        socketConnector.setHandler(new ClientHandler());

        //3.设置连接的一些配置
        socketConnector.setConnectTimeoutMillis(3000);

        connect();
    }

    @Override
    public void connect(){
        if(null!=connection&&!connection.isClosed()) return;
        //4.连接服务器
        ConnectFuture connectFuture = socketConnector.connect(new InetSocketAddress(3301));
        connectFuture.awaitUninterruptibly();//等待连接成功
    }

    @Override
    public void cmdTips() {

    }

    @Override
    public void chat() {

    }

    @Override
    public void close() {

    }

    class ClientHandler extends IoHandlerAdapter {

    }
}
