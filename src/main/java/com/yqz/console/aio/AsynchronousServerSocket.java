package com.yqz.console.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsynchronousServerSocket {

	public static void main(String[] args) {
		try {
			AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open()
					.bind(new InetSocketAddress(8000));

			server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
				final ByteBuffer buffer = ByteBuffer.allocate(1024);

				// accept new connection successfully
				public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {

					try {
						System.out.println("received new remote connection: " + socketChannel.getRemoteAddress());
						buffer.clear();
						socketChannel.read(buffer, null, new CompletionHandler<Integer, Object>() {

							@Override
							public void completed(Integer result, Object attachment) {
								System.out.println("readable data length: " + result);
								if (result > 0) {
									buffer.flip();
									System.out.println(new String(buffer.array()));
									socketChannel.write(buffer);
								}
								buffer.clear();
								socketChannel.read(buffer, null, this);
							}

							@Override
							public void failed(Throwable exc, Object attachment) {
								System.out.println(exc);
							}

						});

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							// server.accept(null, this);
							// writeResult.get();
							// result.close();
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					}
				}

				@Override
				public void failed(Throwable exc, Object attachment) {
					System.out.println("failed: " + exc);
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
