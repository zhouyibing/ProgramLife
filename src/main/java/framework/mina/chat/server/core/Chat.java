package framework.mina.chat.server.core;

import framework.mina.chat.server.manage.Connection;
import framework.mina.chat.server.manage.IConnectManager;
import framework.mina.chat.server.model.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2016/2/23.
 */
public abstract class Chat {
    protected  IConnectManager connectManager;
    private ArrayBlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(100000);
    private ExecutorService executor = Executors.newCachedThreadPool();
    private volatile boolean isClosed;

    protected void putMessage(Message... messages){
        if(isClosed) return;
        try {
            for(Message message :messages)
              messageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setConnectManager(IConnectManager connectManager){
         connectManager = connectManager;
    }

    public abstract void sendMessage(String sendClient,String recivedClient,String content);
    public void close(){
        isClosed = true;
        messageQueue.clear();
        connectManager = null;
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        messageQueue.clear();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isClosed){
                    executor.submit(new Sender());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    class Sender implements Runnable{

        @Override
        public void run() {
            try {
                Message message = messageQueue.take();
                if(null==message) return;
                Connection connection = connectManager.getConnection(message.getReceivedUser());
                boolean result = connection.sendIn(message);
                if(!result) messageQueue.put(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
