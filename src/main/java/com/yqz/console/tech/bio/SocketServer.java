package com.yqz.console.tech.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
	public static void main(String[] args) throws Exception {
		ServerSocket echoServer = null;
		Socket clientSocket = null;
		ExecutorService tp = Executors.newCachedThreadPool();
		try {
			echoServer = new ServerSocket(8000);

			System.out.println(echoServer.getLocalSocketAddress() + " started.");
			while (true) {
				try {
					clientSocket = echoServer.accept();
					System.out.println("remote " + clientSocket.getRemoteSocketAddress() + " connected!");
					tp.execute(new HandleMsg(clientSocket));
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			echoServer.close();
			System.out.println("socket服务端退出");
		}
	}

	static class HandleMsg implements Runnable {
		private Socket clientSocket = null;

		public HandleMsg(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {
			BufferedReader is = null;
			PrintWriter os = null;

			try {
				is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				os = new PrintWriter(clientSocket.getOutputStream(), true);
				// 从InputStream当中读取客户端所发送的数据
				String inputLine = null;
				long b = System.currentTimeMillis();
				while ((inputLine = is.readLine()) != null) {
					System.out.println(clientSocket.getRemoteSocketAddress() + ": " + inputLine);
					os.println(inputLine);
				}
				long e = System.currentTimeMillis();
				System.out.println("spend:" + (e - b) + " ms ");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if (os != null)
					os.close();
				System.out.printf("客户端%s退出\n", clientSocket.getInetAddress());
			}
		}
	}
}
