package framework.mina.chat.server.manage;

import framework.mina.chat.server.model.RunInfo;

/**
 * Created by Zhou Yibing on 2016/2/23.
 * ÁÄÌì·şÎñÆ÷
 */
public interface IChatServer {

    void start();
    void close();
    void reload();
    RunInfo runInfo();
}
