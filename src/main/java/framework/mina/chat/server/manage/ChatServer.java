package framework.mina.chat.server.manage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import framework.mina.chat.demo.CodecFactory;
import framework.mina.chat.server.model.Message;
import framework.mina.chat.server.model.RunInfo;
import framework.mina.chat.server.service.ChatService;
import framework.mina.chat.server.service.IChatService;
import framework.mina.chat.server.service.IUserService;
import framework.mina.chat.server.service.UserService;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Zhou Yibing on 2016/2/23.
 * ���������
 */
public class ChatServer implements IChatServer{

    private SocketAcceptor socketAcceptor;
    private int port;

    private ChatServer(int port){
        socketAcceptor = new NioSocketAcceptor();
    }

    @Override
    public void start(){
        //1.设置过滤器，增加编码工厂
        DefaultIoFilterChainBuilder filterChain  = socketAcceptor.getFilterChain();
        filterChain.addLast("codec",new ProtocolCodecFilter(new CodecFactory()));

        //2.设置handler
        IChatService chatService = new ChatService();
        socketAcceptor.setHandler(new ServerHandler(new MessageHandler(chatService,new UserService(chatService))));

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
    @Override
    public void close(){
       socketAcceptor.dispose();

    }
    @Override
    public void reload(){

    }
    @Override
    public RunInfo runInfo(){
      return null;
    }

    class ServerHandler extends IoHandlerAdapter{
        public MessageHandler messageHandler;

        public ServerHandler() {
        }

        void close(){
          messageHandler.close();
        }

        public ServerHandler(MessageHandler messageHandler) {
            this.messageHandler = messageHandler;
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            super.sessionCreated(session);
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
            session.write("请登录!");
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            super.sessionClosed(session);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            super.sessionIdle(session, status);
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            super.exceptionCaught(session, cause);
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            super.messageReceived(session, message);
            messageHandler.handle(session,message);
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);
        }
    }

    class MessageHandler{
        private IChatService chatService;
        private IUserService userService;

        public MessageHandler(IChatService chatService, IUserService userService) {
            this.chatService = chatService;
            this.userService = userService;
        }

        public void handle(IoSession ioSession,Object message){
            Message m = (Message) JSONObject.parse(message.toString());
            Message.MessageType messageType = m.getMessageType();
            String content = m.getContent();
            JSONObject contentJson = JSON.parseObject(content);
            if(messageType== Message.MessageType.CMD) {
                String cmd = contentJson.getString("cmd");
                if (cmd.equalsIgnoreCase("login")) {
                    String userName = contentJson.getString("userName");
                    userService.login(userName, ioSession);
                } else if (cmd.equalsIgnoreCase("logout")) {
                    userService.loginOut(contentJson.getString("userName"));
                }
            }else if(messageType== Message.MessageType.GROUP){
                chatService.sendRoomMessage(m.getSendUser(),m.getRelationId(),m.getContent());
            }else if(messageType == Message.MessageType.PRIVATE){
                chatService.send2MyFriend(m.getSendUser(),m.getReceivedUser(),m.getContent());
            }
        }

        public void close() {
            chatService.close();
            userService = null;
            chatService = null;
        }
    }
}
