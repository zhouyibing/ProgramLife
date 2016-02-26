package framework.mina.chat.server.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by Zhou Yibing on 2016/2/23.
 * �û�
 */
@Data
public class User {
    private String userId;
    private String password;
    private String loginIp;
    private Date loginTime;
    private boolean isOnline;
}
