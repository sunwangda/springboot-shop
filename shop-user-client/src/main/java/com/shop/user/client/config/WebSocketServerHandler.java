package com.shop.user.client.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{
    
    //在线人存储
    private static final Map<String, NioSocketChannel> channelMap = new ConcurrentHashMap<>(16);
    
    //保存全局的，连接上服务器的客户
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    
    private static Logger log = LoggerFactory.getLogger(WebSocketServerHandler.class);

    private WebSocketServerHandshaker handshaker;
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //http请求和tcp请求分开处理
        if(msg instanceof HttpRequest){
            handlerHttpRequest(ctx,(HttpRequest) msg);
        }else if(msg instanceof WebSocketFrame){
            handlerWebSocketFrame(ctx,(WebSocketFrame) msg);
        }
    }
    
    
    /**
     * 
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    /**
     * websocket消息处理
     * @param ctx
     * @param msg
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是关闭链路的指令
        if(frame instanceof CloseWebSocketFrame){
            log.info("【"+ctx.channel().remoteAddress()+"】已关闭（服务器端）");
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame);
            //移除channel
            removeCannel((NioSocketChannel)ctx.channel());
            return;
        }
        //判断是否是ping消息
        if(frame instanceof PingWebSocketFrame){
            log.info("【ping】");
            //PongWebSocketFrame pong = new PongWebSocketFrame(frame.content().retain());
            //ctx.channel().writeAndFlush(pong);
            return ;
        }
        //判断实时是pong消息
        if(frame instanceof PongWebSocketFrame){
            log.info("【pong】");
            return ;
        }
        //本例子只支持文本，不支持二进制
        if(!(frame instanceof TextWebSocketFrame)){
            log.info("【不支持二进制】");
            throw new UnsupportedOperationException("不支持二进制");
        }
        //返回信息应答
        JSONObject object = JSONObject.parseObject(((TextWebSocketFrame) frame).text().toString());
        
        //接收信息的人是否在线
        if(channelMap.containsKey(object.getString("toUser"))){
            //在线时直接发送,已送达
            //只支持文本形式，信息必须以文本形式发送
            channelMap.get(object.getString("toUser")).writeAndFlush(new TextWebSocketFrame(object.toString()));
        }
            
        
    }
    
     /**
     * wetsocket第一次连接握手
     * @param ctx
     * @param msg
     */
    @SuppressWarnings("deprecation")
    private void handlerHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
        String userUid = null;
        if (req.getMethod().toString().equals("GET")) {
            userUid = req.getUri().substring(req.getUri().indexOf("/", 2)+1);
            //对用户信息进行存储
            channelMap.put(userUid, (NioSocketChannel)ctx.channel());           
        }
        
        // http 解码失败
        if(!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))){
            sendHttpResponse(ctx, (FullHttpRequest) req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.BAD_REQUEST));
        }
        //可以通过url获取其他参数
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(
                "ws://"+req.headers().get("Host")+"/"+req.getUri()+"",null,false
        );
        handshaker = factory.newHandshaker(req);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        }else{
            //进行连接
            handshaker.handshake(ctx.channel(), (FullHttpRequest) req);    
            //拉取未发送的数据
            //TODO
        }
    }

    @SuppressWarnings("deprecation")
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
    
     /**
     * 这里是保持服务器与客户端长连接  进行心跳检测 避免连接断开
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            //PingWebSocketFrame ping = new PingWebSocketFrame();
            switch (stateEvent.state()){
                //读空闲（服务器端）
                case READER_IDLE:
                    log.info("【"+ctx.channel().remoteAddress()+"】读空闲（服务器端）");
                    //ctx.writeAndFlush(ping);
                    break;
                    //写空闲（客户端）
                case WRITER_IDLE:
                    log.info("【"+ctx.channel().remoteAddress()+"】写空闲（客户端）");
                    //ctx.writeAndFlush(ping);
                    break;
                case ALL_IDLE:
                    log.info("【"+ctx.channel().remoteAddress()+"】读写空闲");
                    break;
            }
        }
    }

    /**
     * 出现异常时
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //移除channel
        removeCannel((NioSocketChannel)ctx.channel());
        ctx.close();
        log.info("【"+ctx.channel().remoteAddress()+"】已关闭（服务器端）");
    }
    
    /**
     * 从缓存中移除已关闭的channel
     * @param nioSocketChannel
     */
    private void removeCannel(NioSocketChannel nioSocketChannel){
        //从当前在线中移除
        if(channelMap.containsValue(nioSocketChannel)){
            for(Map.Entry<String, NioSocketChannel> entry : channelMap.entrySet()){
                if(entry.getValue() == nioSocketChannel){
                    channelMap.remove(entry.getKey());
                    break;
                }
            }
        }
    }

}
