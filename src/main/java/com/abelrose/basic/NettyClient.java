package com.abelrose.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

// 网络客户端类
public class NettyClient {
    public static void main(String[] args) throws Exception{

        //1. 客户端需要一个线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2. 创建客户端启动助手
        Bootstrap b = new Bootstrap();
        //3. 配置
        b.group(group) //3. 设置线程
         .channel(NioSocketChannel.class) //4. 设置客户端通道实现类
         .handler(new ChannelInitializer<SocketChannel>() { // 5. 创建一个通道初始化对象
             @Override
             protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NettyClientHandler()); // 往Pipeline链中添加自定义handler
             }
         });
        System.out.println("-----Client is Ready-------");
        // 7. 启动客户端连接服务器端(异步非阻塞)
        ChannelFuture cf = b.connect("127.0.0.1", 9999).sync(); // connect是异步的 sync方法是同步阻塞的
        // 8. 关闭连接(异步非阻塞)
        cf.channel().closeFuture().sync();
    }
}
