package com.yqz.console.tech.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.locks.LockSupport;

public class SocketClient {
	public static void main(String[] args) throws Exception {
		Socket client = null;
		PrintWriter writer = null;
		try {
			client = new Socket();
			client.connect(new InetSocketAddress("localhost", 8000));
			writer = new PrintWriter(client.getOutputStream(), true);
			writer.println("Hello!!!");

			// received msg
			MessageReader messageReader = new MessageReader(client);
			messageReader.start();

			int i = 0;
			while (true) {
				// byte[] bs = new byte[1024];
				// int i = System.in.read(bs);
				// String s = new String(bs);
				// s = s.replaceAll("\r\n", "");
				LockSupport.parkNanos(1000);
				writer.println(Thread.currentThread().getName() + "_" + ++i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
			if (writer != null)
				writer.close();
		}
		System.out.println("client exists. ");

	}

	static class MessageReader extends Thread {
		private BufferedReader reader = null;
		private Socket client = null;

		public MessageReader(Socket socket) throws IOException {
			this.client = socket;
			this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		}

		@Override
		public void run() {
			while (true) {
				try {
					char[] chars = new char[1024];
					int count = this.reader.read(chars);

					System.out.println(String.format("echo(%d): %s", count, new String(chars)));
				} catch (IOException e) {
					e.printStackTrace();
					try {
						if (this.reader != null)
							this.reader.close();
						if (this.client != null)
							this.client.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}

		}

	}
}
