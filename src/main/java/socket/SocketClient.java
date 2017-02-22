package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketClient {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8080);
			while (true) {
				// 一旦有堵塞, 则表示服务器与客户端获得了连接
				Socket client = serverSocket.accept();
				byte[] bytes = new byte[1024];
				client.getInputStream().read(bytes);
				System.out.println(new String(bytes));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				serverSocket.close();
			}
		}
	}
}
