package com.abelrose.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

//服务器端的业务处理类(读/写数据)
public class NettyServerHandler extends ChannelInboundHandlerAdapter { // Inbound 服务器端主要是读书库 所以是In

    /**
     * 读取数据事件
     * @param ctx
     * @param msg 客户端发过来的事件
     */
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        System.out.println("Server ctx:" + ctx);
        ByteBuf buf = (ByteBuf) msg; // 这个数据其实还是以缓冲区的形式传过来的 注意是ByteBuf 不是 ByteBuffer
        System.out.println("客户端发来的消息:" + buf.toString(CharsetUtil.UTF_8)); // 缓冲区抓换成字符抓 并且设置编码
    }

    // 数据读取完毕事件
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("就是没有钱",CharsetUtil.UTF_8));
    }

    // 异常发生事件
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        ctx.close(); // 发生异常的狮虎直接关闭上下文对象就可以
    }


}
