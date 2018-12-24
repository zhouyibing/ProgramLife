package zhou.base.nio.netty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class Client implements CompletionHandler<Void,Client>,Runnable{

        private String host;
        private int port;
        private AsynchronousSocketChannel channel;
        private CountDownLatch countDownLatch;
        public Client(String host,int port){
            this.host=host;
            this.port=port;

        }

        @Override
        public void run() {
            try {
                channel = AsynchronousSocketChannel.open();
                countDownLatch = new CountDownLatch(1);
                channel.bind(new InetSocketAddress(host,8090));
                channel.connect(new InetSocketAddress(host,port),this,this);
                countDownLatch.await();
                channel.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        public void completed(Void result, Client attachment) {
            ByteBuffer buffer = null;
            try {
                buffer = ByteBuffer.wrap("hello!".getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if(attachment.hasRemaining()){
                        channel.write(attachment,attachment,this);
                    }else{
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                attachment.flip();
                                byte[] bytes = new byte[attachment.remaining()];
                                attachment.get(bytes);
                                try {
                                    System.out.println(new String(bytes,"UTF-8"));
                                    countDownLatch.countDown();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                exc.printStackTrace();
                                try {
                                    channel.close();
                                    countDownLatch.countDown();
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                    try {
                        channel.close();
                        countDownLatch.countDown();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void failed(Throwable exc, Client attachment) {
            try {
                channel.close();
                countDownLatch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public static void main(String[] args){
        Client client = new Client("localhost",8000);
        new Thread(client,"aio-client").start();
    }
}