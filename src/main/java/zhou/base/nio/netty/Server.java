package zhou.base.nio.netty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class Server implements Runnable{
        private int port;
        private CountDownLatch countDownLatch;
        private AsynchronousServerSocketChannel channel;
        public Server(int port){
            this.port=port;
        }

        @Override
        public void run() {
            try {
                channel=AsynchronousServerSocketChannel.open();
                channel.bind(new InetSocketAddress("localhost",port));
                System.out.println("server["+Thread.currentThread().getName()+"] started at port:"+port);
                countDownLatch = new CountDownLatch(1);
                doAccept();
                countDownLatch.await();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        private void doAccept(){
            channel.accept(this, new CompletionHandler<AsynchronousSocketChannel, Server>() {
                @Override
                public void completed(final AsynchronousSocketChannel result, Server attachment) {
                    attachment.channel.accept(attachment,this);
                    ByteBuffer read = ByteBuffer.allocate(1024);
                    result.read(read, read, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            byte[] bytes = new byte[attachment.remaining()];
                            attachment.get(bytes);
                            String req = null;
                            try {
                                req = new String(bytes,"UTF-8");
                                doWrite("I hava received your message:"+req);
                            } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                            }
                        }

                        private void doWrite(String s) {
                            if(null!=s&&s.trim().length()>0){
                                final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes());
                                buffer.flip();
                                result.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                                    @Override
                                    public void completed(Integer r, ByteBuffer attachment) {
                                        if(buffer.hasRemaining())
                                            result.write(buffer,buffer,this);
                                    }

                                    @Override
                                    public void failed(Throwable exc, ByteBuffer attachment) {
                                        exc.printStackTrace();
                                        try {
                                            result.close();
                                        } catch (IOException e) {
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
                                result.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void failed(Throwable exc, Server attachment) {
                    exc.printStackTrace();
                    attachment.countDownLatch.countDown();
                }
            });
        }
        public static void main(String[] args){
            Server server = new Server(8000);
            new Thread(server,"aio-server").start();
        }
}