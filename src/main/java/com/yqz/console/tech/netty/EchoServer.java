package com.yqz.console.tech.netty;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import com.yqz.console.tech.netty.codes.MessagePackDecoder;
import com.yqz.console.tech.netty.codes.MessagePackEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class EchoServer {
	private final int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
			return;
		}
		int port = Integer.parseInt(args[0]); // 1
		new EchoServer(port).start(); // 2
	}

	public void start() throws Exception {
		NioEventLoopGroup group = new NioEventLoopGroup(); // 3
		try {
			ServerBootstrap b = new ServerBootstrap().group(group) // 4
					.channel(NioServerSocketChannel.class) // 5
					.localAddress(new InetSocketAddress(port)) // 6
					.childHandler(new ChannelInitializer<SocketChannel>() { // 7
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							// ch.pipeline().addLast(new
							// LineBasedFrameDecoder(1024))
							// .addLast(new
							// StringDecoder(Charset.forName("utf-8")))
							// .addLast(new EchoServerHandler());

							ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
									.addLast(new MessagePackDecoder()).addLast(new LengthFieldPrepender(2))
									.addLast(new MessagePackEncoder()).addLast(new EchoServerHandler());
						}
					});

			ChannelFuture f = b.bind().sync(); // 8
			f.addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					System.out.println("ChannelFuture is completed." + future);
					if (future.isSuccess()) { // 3
						ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset()); // 4
						ChannelFuture wf = future.channel().writeAndFlush(buffer); // 5
					} else {
						Throwable cause = future.cause(); // 6
						cause.printStackTrace();
					}
				}

			});
			System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
			f.channel().closeFuture().sync(); // 9
		} finally {
			group.shutdownGracefully().sync(); // 10
		}
	}
}
