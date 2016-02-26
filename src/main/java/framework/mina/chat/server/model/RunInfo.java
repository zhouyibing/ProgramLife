package framework.mina.chat.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Zhou Yibing on 2016/2/23.
 */
@Data
public class RunInfo implements Serializable{
    private Long activeUser;//��ǰ��û���
    private Long connectCount;//������
    private Long messageCount;//��Ϣ��
    private Long activeRoom;//��ǰ���������
    private ThreadInfo[] threadInfos;//�������߳���Ϣ
    private ThreadGroupInfo[] threadGroupInfos;//�������߳�����Ϣ

    //�������߳���Ϣ
    class ThreadInfo{
        private String threadName;
        private Status status;
    }

    class ThreadGroupInfo{
        private String groupName;
        private int activeCount;
        private int waitingCount;
        private int blockCount;
        private int queueCount;
    }

    enum Status{
        ACTIVE(0),BLOCK(1),WAITING(2),DEAD(3),DEADLOCK(4);

        private int code;
        Status(int code) {
            this.code = code;
        }
        public int getCode(){return code;}
    }

}
