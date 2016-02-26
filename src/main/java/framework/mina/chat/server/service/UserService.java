package framework.mina.chat.server.service;

import framework.mina.chat.server.manage.ConnectManager;
import framework.mina.chat.server.model.User;
import org.apache.mina.core.session.IoSession;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Zhou Yibing on 2016/2/25.
 */
public class UserService implements IUserService{

    private ConcurrentHashMap<String,User> userMap = new ConcurrentHashMap<>();
    private IChatService chatService;

    public IChatService getChatService() {
        return chatService;
    }

    public UserService(IChatService chatService) {
        this.chatService = chatService;
    }

    public void setChatService(IChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void login(String userName, IoSession session) {
        if(userMap.containsKey(userName)) chatService.send2MyFriend("server",userName,"该用户已登录,请重新设置用户名");
        User user = new User();
        user.setUserId(userName);
        user.setLoginIp(session.getRemoteAddress().toString());
        user.setLoginTime(new Date());
        user.setOnline(true);
        userMap.put(userName,user);
        ConnectManager.getConnectManager().createConnection(user,session);
        chatService.send2MyFriend("server",userName,"登录成功");
    }

    @Override
    public void loginOut(String userName) {
        userMap.remove(userName);
        ConnectManager.getConnectManager().disconnect(userName);
        chatService.send2MyFriend("server",userName,"登出成功");
    }
}
