package com.yqz.console.tech.netty.codes;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessagePackDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		final byte[] bytes = new byte[in.readableBytes()];
		in.getBytes(in.readerIndex(), bytes, 0, bytes.length);
		MessagePack mp = new MessagePack();
		out.add(mp.read(bytes));
	}

}
