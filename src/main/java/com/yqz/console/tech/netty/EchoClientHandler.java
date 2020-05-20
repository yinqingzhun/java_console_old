package com.yqz.console.tech.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<String> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		String[] ss = { "hi", "hello", "Is there anybody there?", "this is client" };
		for (int i = 0; i < ss.length; i++) {

			ctx.writeAndFlush(Unpooled.copiedBuffer(ss[i] + System.getProperty("line.separator"), // 2
					CharsetUtil.UTF_8));
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String in) {
		System.out.println("Client received: " + in.toString()); // 3
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // 4
		cause.printStackTrace();
		ctx.close();
	}

}
