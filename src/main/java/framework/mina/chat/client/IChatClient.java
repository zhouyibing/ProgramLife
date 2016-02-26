package framework.mina.chat.client;

/**
 * Created by Zhou Yibing on 2016/2/26.
 */
public interface IChatClient {
    void connect();

    void cmdTips();
    void chat();
    void start();
    void close();
}
