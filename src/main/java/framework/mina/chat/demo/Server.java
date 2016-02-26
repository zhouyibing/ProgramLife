package framework.mina.chat.demo;

import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Zhou Yibing on 2016/2/24.
 */
    //服务器端
public  class Server{
        SocketAcceptor socketAcceptor;
        int port;

        public Server(int port) {
            this.socketAcceptor = new NioSocketAcceptor();
            this.port=port;
        }

        public void start(){
            //1.设置过滤器，增加编码工厂
            DefaultIoFilterChainBuilder filterChain  = socketAcceptor.getFilterChain();
            filterChain.addLast("codec",new ProtocolCodecFilter(new CodecFactory()));

            //2.设置handler
            socketAcceptor.setHandler(new ServerHandler());

            //3.设置sesssion配置
            socketAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,30);
            socketAcceptor.getSessionConfig().setReadBufferSize(2048);

            //4.绑定端口
            try {
                socketAcceptor.bind(new InetSocketAddress(port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    //服务器端handler
     class ServerHandler extends IoHandlerAdapter{
        private final  Logger logger = LoggerFactory.getLogger(ServerHandler.class);
        @Override
        public void sessionCreated(IoSession session) throws Exception {
            logger.info("创建session:"+session.getId()+"-"+session.getRemoteAddress()+")");
            session.write("欢迎进入聊天室");
            Collection<IoSession> sessionCollection = session.getService().getManagedSessions().values();
            for(IoSession ioSession:sessionCollection) {
                if (ioSession.getId() != session.getId())
                    ioSession.write(session.getId()+"-"+session.getRemoteAddress()+"上线啦");
            }
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
            logger.info("session(" + session.getId() + "-"+session.getRemoteAddress()+")出现异常:" + cause.getMessage());
        }


        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            logger.info("服务端从session("+session.getId()+"-"+session.getRemoteAddress()+")获取消息:" + message.toString());
            if(message.toString().equals("quit")) {
                sessionClosed(session);
                message = session.getId()+"-"+session.getRemoteAddress()+"已离线";
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String sendTime = sf.format(new Date());
            Collection<IoSession> sessionCollection = session.getService().getManagedSessions().values();
            for(IoSession ioSession:sessionCollection){
                if(ioSession.getId()!=session.getId())
                ioSession.write(sendTime+" 转发"+session.getId()+"-"+session.getRemoteAddress()+"的消息:"+message);
            }
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            logger.info("服务端发送消息给" + session.getId() + "-" + session.getRemoteAddress() + ":" + message.toString());
        }
    }

        public static void main(String[] args){
            PropertyConfigurator.configure("src/main/java/log4j.properties");
            Server server = new Server(3301);
            server.start();
        }
}
