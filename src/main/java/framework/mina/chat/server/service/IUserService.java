package framework.mina.chat.server.service;


import org.apache.mina.core.session.IoSession;

/**
 * Created by Zhou Yibing on 2016/2/23.
 */
public interface IUserService {
    void login(String userName, IoSession session);

    void loginOut(String userName);
}
