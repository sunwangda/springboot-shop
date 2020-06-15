package com.shop.user.client.config;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class NettyWebsocketServer {
    
        
    /**
     * EventLoop接口
     * NioEventLoop中维护了一个线程和任务队列，支持异步提交执行任务，线程启动时会调用NioEventLoop的run方法，执行I/O任务和非I/O任务：
     * I/O任务
     * 即selectionKey中ready的事件，如accept、connect、read、write等，由processSelectedKeys方法触发。
     * 非IO任务
     * 添加到taskQueue中的任务，如register0、bind0等任务，由runAllTasks方法触发。
     * 两种任务的执行时间比由变量ioRatio控制，默认为50，则表示允许非IO任务执行的时间与IO任务的执行时间相等。
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    /**
     * Channel
     * Channel类似Socket，它代表一个实体（如一个硬件设备、一个网络套接字）的开放连接，如读写操作。通俗地讲，Channel字面意思就是通道，每一个客户端与服务端之间进行通信的一个双向通道。
     * Channel主要工作：
     * 1.当前网络连接的通道的状态（例如是否打开？是否已连接？）
     * 2.网络连接的配置参数 （例如接收缓冲区大小）
     * 3.提供异步的网络 I/O 操作(如建立连接，读写，绑定端口)，异步调用意味着任何 I/O 调用都将立即返回，并且不保证在调用结束时所请求的 I/O 操作已完成。
     *  调用立即返回一个 ChannelFuture 实例，通过注册监听器到ChannelFuture 上，可以 I/O 操作成功、失败或取消时回调通知调用方。
     * 4.支持关联 I/O 操作与对应的处理程序。
     * 不同协议、不同的阻塞类型的连接都有不同的 Channel 类型与之对应，下面是一些常用的 Channel 类型
     * NioSocketChannel，异步的客户端 TCP Socket 连接
     * NioServerSocketChannel，异步的服务器端 TCP Socket 连接
     * NioDatagramChannel，异步的 UDP 连接
     * NioSctpChannel，异步的客户端 Sctp 连接
     * NioSctpServerChannel，异步的 Sctp 服务器端连接
     * 这些通道涵盖了 UDP 和 TCP网络 IO以及文件 IO.
     */
    private Channel channel;
    
    /**
     * 启动服务
     * @param port
     */
    public void start(int port){
        
        /**
         * Future
         * Future提供了另外一种在操作完成是通知应用程序的方式。这个对象可以看作一个异步操作的结果占位符。
         * 通俗地讲，它相当于一位指挥官，发送了一个请求建立完连接，通信完毕了，你通知一声它回来关闭各项IO通道，整个过程，它是不阻塞的，异步的。
         * 在Netty中所有的IO操作都是异步的，不能立刻得知消息是否被正确处理，但是可以过一会等它执行完成或者直接注册一个监听，具体的实现就是通过Future和ChannelFutures，
         * 他们可以注册一个监听，当操作执行成功或失败时监听会自动触发注册的监听事件。
         */
        try {
            /**
             * Bootstrap
             * Bootstrap是引导的意思，一个Netty应用通常由一个Bootstrap开始，
             * 主要作用是配置整个Netty程序，串联各个组件，
             * Netty中Bootstrap类是客户端程序的启动引导类，
             * ServerBootstrap是服务端启动引导类。
             */
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup,workGroup)
                  //非阻塞
                  .channel(NioServerSocketChannel.class)
                  //设置为前端websocket可以连接
                  .childHandler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    // HttpServerCodec：将请求和应答消息解码为HTTP消息
                    pipeline.addLast("http-codec",new HttpServerCodec());
                    //将HTTP消息的多个部分合成一条完整的HTTP消息
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                    //向客户端发送HTML5文件
                    socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    // 进行设置心跳检测
                    socketChannel.pipeline().addLast(new IdleStateHandler(60,30,60*30, TimeUnit.SECONDS));
                    //配置通道处理  来进行业务处理
                    pipeline.addLast("handler", new WebSocketServerHandler());
                }
                
            });
            channel = server.bind(port).sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 停止服务
     */
    public void destroy(){
        if(channel != null) { 
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

//    public static void main(String[] args) {
//        NettyWebsocketServer server = new NettyWebsocketServer();
//        System.out.println("看看main函数是否运行什么时候运行");
//        server.start(7788);
//    }
}