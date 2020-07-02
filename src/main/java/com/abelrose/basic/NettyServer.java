package com.abelrose.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

// 服务器端代码
public class NettyServer {
    public static void main(String[] args) throws Exception{

        //1. 创建一个线程组 用于接收客户端连接
        EventLoopGroup bossGruop = new NioEventLoopGroup();
        //2, 创建一个线程组 用于处理网络请求
        EventLoopGroup workerGruop = new NioEventLoopGroup();
        //3. 创建服务器端启动助手 来配置参数
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGruop,workerGruop) //4. 设置两个线程组
         .channel(NioServerSocketChannel.class) //5. Netty 底层使用 NioSctpServerChannel 作为服务器端通道的实现
         .option(ChannelOption.SO_BACKLOG,128) //6.设置线程队列中等待的个数
         .childOption(ChannelOption.SO_KEEPALIVE,true) //7. 保持活动的连接状态
         .childHandler(new ChannelInitializer<SocketChannel>() { //8.创建一个通道初始化对象
             @Override
             protected void initChannel(SocketChannel sc) throws Exception { //9. 在pipline中加入自定义的Handler类
                sc.pipeline().addLast(new NettyServerHandler());
             }
         });
        System.out.println("------Server is Ready-----");
        ChannelFuture cf = b.bind(9999).sync(); //10.绑定端口 设置为非阻塞  注意bind方法是异步的(不需要是否绑定成功 是否产生了一个结果) sync是同步的
        System.out.println("------Server is starting-----");

        //11. 关闭通道 关闭线程组
        cf.channel().closeFuture().sync(); // 异步

        //关闭线程池
        bossGruop.shutdownGracefully();
        workerGruop.shutdownGracefully();
    }
}
