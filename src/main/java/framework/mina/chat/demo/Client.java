package framework.mina.chat.demo;

import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created by Zhou Yibing on 2016/2/24.
 */
//客户端
public class Client{

    private SocketConnector socketConnector;
    private IoSession iosession;
    private ConnectFuture connectFuture;
    public Client(){
        socketConnector = new NioSocketConnector();
    }

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

    public void connect(){
        if(null!=iosession&&!iosession.isClosing()) return;
        //4.连接服务器
        connectFuture = socketConnector.connect(new InetSocketAddress(3301));
        connectFuture.awaitUninterruptibly();//等待连接成功
        iosession = connectFuture.getSession();//获得session
    }

    public void send(Object message){
        if(null==iosession)
            System.out.println("您已与服务器断开连接");
        iosession.write(message);
    }

    public void setAttribute(Object key,Object value){
        iosession.setAttribute(key,value);
    }

    public Object getAttribute(String key){
        return iosession.getAttribute(key);
    }

    public IoSession getIosession(){
        return iosession;
    }

    public void close(){
        CloseFuture closeFuture = iosession.getCloseFuture();
        closeFuture.awaitUninterruptibly(1000);
        socketConnector.dispose();
    }
}

//客户端handler
 class ClientHandler extends IoHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.info("创建session:"+session.getId()+"-"+session.getRemoteAddress()+")");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logger.info("session("+session.getId()+"-"+session.getRemoteAddress()+")打开");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("session("+session.getId()+"-"+session.getRemoteAddress()+")关闭");
        CloseFuture closeFuture = session.close(true);
        closeFuture.addListener(new IoFutureListener<IoFuture>() {
            @Override
            public void operationComplete(IoFuture future) {
                if(future instanceof  CloseFuture){
                    ((CloseFuture) future).setClosed();
                    logger.info("sessionClosed CloseFuture setClosed-->{},",future.getSession().getId()+"-"+future.getSession().getRemoteAddress());
                }
            }
        });
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.info("session("+session.getId()+"-"+session.getRemoteAddress()+")空闲:"+status.toString());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.info("session(" + session.getId() +"-"+session.getRemoteAddress()+")出现异常:" + cause.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        logger.info("从"+session.getId()+"-"+session.getRemoteAddress()+"获取消息:"+message.toString());
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("客户端发送消息:" + message);
    }
    public static void main(String[] args){
        PropertyConfigurator.configure("src/main/java/log4j.properties");
        Client client = new Client();
        try {
            client.start();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();
            if(message.equals("connect"))
                client.connect();
            else if(message.equals("exit")) {
                client.close();
                System.exit(0);
            }else if(message.equals("key")){
                client.setAttribute("key","aaaa");
            }else if(message.equals("getKey")){
                System.out.println(client.getAttribute("key"));
            }
            else
                client.send(message);
        }
    }
}