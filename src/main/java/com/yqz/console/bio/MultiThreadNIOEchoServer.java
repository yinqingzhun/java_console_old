package com.yqz.console.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadNIOEchoServer {
	public static Map<Socket, Long> socketTimeStat = new HashMap<Socket, Long>();

	class EchoMessages {
		private LinkedList<ByteBuffer> outputQueue;

		EchoMessages() {
			outputQueue = new LinkedList<ByteBuffer>();
		}

		public LinkedList<ByteBuffer> getOutputQueue() {
			return outputQueue;
		}

		public void enqueue(ByteBuffer bb) {
			outputQueue.push(bb);
		}
	}

	class HandleMsg implements Runnable {
		SelectionKey selectionKey;
		ByteBuffer byteBuffer;

		public HandleMsg(SelectionKey sk, ByteBuffer bb) {
			this.selectionKey = sk;
			this.byteBuffer = bb;
		}

		@Override
		public void run() {
			EchoMessages echoClient = (EchoMessages) selectionKey.attachment();
			echoClient.enqueue(byteBuffer);
			selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			selector.wakeup();
		}

	}

	private Selector selector;
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private ServerSocketChannel serverChannel = null;

	private void startServer() throws Exception {
		selector = SelectorProvider.provider().openSelector();
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(new InetSocketAddress(8000));
		// 注册感兴趣的事件，此处对accpet事件感兴趣
		SelectionKey acceptKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		for (;;) {
			selector.select();
			Iterator<SelectionKey> readyKeys = selector.selectedKeys().iterator();
			long e = 0;
			while (readyKeys.hasNext()) {
				SelectionKey selectionKey = (SelectionKey) readyKeys.next();
				readyKeys.remove();

				SocketAddress addr = null;
				if (selectionKey.channel() instanceof SocketChannel) {
					SocketChannel channel = (SocketChannel) selectionKey.channel();
					addr = channel.socket().getRemoteSocketAddress();
				} else if (selectionKey.channel() instanceof ServerSocketChannel) {
					ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
					addr = channel.socket().getLocalSocketAddress();
				}

				System.out.println(addr + ": " + (selectionKey.isAcceptable() ? "Acceptable " : "")
						+ (selectionKey.isReadable() ? "Readable " : "")
						+ (selectionKey.isWritable() ? "Writable" : ""));

				if (selectionKey.isAcceptable()) {
					doAccept(selectionKey);
				}

				if (selectionKey.isValid() && selectionKey.isReadable()) {
					if (!socketTimeStat.containsKey(((SocketChannel) selectionKey.channel()).socket())) {
						socketTimeStat.put(((SocketChannel) selectionKey.channel()).socket(),
								System.currentTimeMillis());
					}
					doRead(selectionKey);
				}

				if (selectionKey.isValid() && selectionKey.isWritable()) {
					doWrite(selectionKey);
					e = System.currentTimeMillis();
					long b = socketTimeStat.remove(((SocketChannel) selectionKey.channel()).socket());
					System.out.println("spend:" + (e - b) + "ms");
				}
			}
		}
	}

	private void doWrite(SelectionKey sk) {
		SocketChannel channel = (SocketChannel) sk.channel();
		EchoMessages echoClient = (EchoMessages) sk.attachment();
		LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();
		ByteBuffer bb = outq.peek();
		if (bb == null)
			System.out.println("no data waiting to send");
		try {
			int len = channel.write(bb);

			if (bb.remaining() == 0) {
				outq.poll();
			}
		} catch (Exception e) {
			e.printStackTrace();
			disconnect(sk);
		}
		if (outq.size() == 0) {
			sk.interestOps(SelectionKey.OP_READ);
		}
	}

	private void doRead(SelectionKey selectionKey) {
		SocketChannel channel = (SocketChannel) selectionKey.channel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
		int len;
		try {
			len = channel.read(byteBuffer);
			if (len < 0) {
				disconnect(selectionKey);
				return;
			}
		} catch (Exception e) {
			disconnect(selectionKey);
			return;
		}
		byteBuffer.flip();
		threadPool.execute(new HandleMsg(selectionKey, byteBuffer));
	}

	private void disconnect(SelectionKey sk) {
		if (sk != null && sk.isValid()) {
			try {
				sk.channel().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 接受来自客户端的新连接，将连接通道channel注册到选择器selector
	 * 
	 * @param selectionKey
	 */
	private void doAccept(SelectionKey selectionKey) {
		ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
		SocketChannel clientChannel;
		try {
			clientChannel = server.accept();
			clientChannel.configureBlocking(false);
			SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);

			EchoMessages echoClinet = new EchoMessages();
			clientKey.attach(echoClinet);

			System.out.println("Accepted connection from " + clientChannel.socket().getInetAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MultiThreadNIOEchoServer echoServer = new MultiThreadNIOEchoServer();
		try {
			echoServer.startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
