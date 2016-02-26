package framework.mina.chat.server.manage;

import framework.mina.chat.server.model.Message;
import framework.mina.chat.server.model.User;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Zhou Yibing on 2016/2/23.
 * 用户登录成功后就会创建一个connection,通过connection向用户发送消息
 */
public class Connection {
    private final static Logger logger = LoggerFactory.getLogger(Connection.class);
    private User user;
    private IoSession ioSession;
    private volatile boolean isClosed;

    public Connection(User user, IoSession ioSession) {
        this.user = user;
        this.ioSession = ioSession;
        this.isClosed = false;
    }

    public boolean sendIn(Message message){
        if(isClosed) return false;
        message.setReceivedUser(user.getUserId());
        message.setSendTime(new Date());
        ioSession.write(message);
        return true;
    }

    public boolean sendOut(Message message){
        if(isClosed) return false;
        message.setSendUser(user.getUserId());
        message.setSendTime(new Date());
        ioSession.write(message);
        return true;
    }

    public void disconnect(){
        CloseFuture closeFuture = ioSession.close(true);
        closeFuture.addListener(new IoFutureListener<IoFuture>() {
            @Override
            public void operationComplete(IoFuture future) {
                if(future instanceof  CloseFuture){
                    ((CloseFuture) future).setClosed();
                    logger.info("用户"+user.getUserId()+"断开了连接");
                    user.setOnline(false);
                    isClosed=true;
                }
            }
        });
    }

   public boolean isClosed(){
        return isClosed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IoSession getIoSession() {
        return ioSession;
    }

    public void setIoSession(IoSession ioSession) {
        this.ioSession = ioSession;
    }
}
