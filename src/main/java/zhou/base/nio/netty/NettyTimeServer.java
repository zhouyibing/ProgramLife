package zhou.base.nio.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;

public class NettyTimeServer {

    public static void main(String[] args){
        final int port = 8080;
        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new TimeServer().bind(port);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new TimeClient().connect("127.0.0.1",port);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        server.start();
        client.start();
    }
    static class TimeServer{
        public void bind(int port) throws InterruptedException {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                                socketChannel.pipeline().addLast(new StringDecoder());
                                socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        /*ByteBuf buf = (ByteBuf)msg;
                                        byte[] req = new byte[buf.readableBytes()];
                                        buf.readBytes(req);
                                        String body = new String(req,"UTF-8");*/
                                        String body = (String)msg;
                                        System.out.println("The time server receive order:"+body);
                                        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
                                        currentTime=currentTime+System.getProperty("line.separator");
                                        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
                                        ctx.write(resp);
                                    }

                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                        cause.printStackTrace();
                                        ctx.close();
                                    }

                                    @Override
                                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                        ctx.flush();
                                    }
                                });
                            }
                    });
            //wait for bind port success.
            ChannelFuture f = null;
            try {
                f = bootstrap.bind(port).sync();
                f.channel().closeFuture().sync();
            }finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    }

    static class TimeClient{
        public void connect(String host,int port) throws InterruptedException {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    byte[] req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
                                    for(int i=0;i<100;i++) {
                                        ByteBuf buf = Unpooled.buffer(req.length);
                                        buf.writeBytes(req);
                                        ctx.writeAndFlush(buf);
                                    }
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    /*ByteBuf buf = (ByteBuf)msg;
                                    byte[] req = new byte[buf.readableBytes()];
                                    buf.readBytes(req);
                                    String body = new String(req,"UTF-8");*/
                                    System.out.println("Now is:"+msg);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });
                        }
                    });
            ChannelFuture f= null;
            try {
                f = bootstrap.connect(host,port).sync();
                f.channel().closeFuture().sync();
            } finally {
                group.shutdownGracefully();
            }
        }
    }
}
