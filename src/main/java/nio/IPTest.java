package nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class IPTest {

	//客户端
	@SuppressWarnings("resource")
	@Test
	public void clien() throws Exception, IOException {
		// TODO Auto-generated method stub
		String str="心好累wd";
		Socket socket = new Socket(InetAddress.getLocalHost(), 9898);
		OutputStream stream = socket.getOutputStream();
		stream.write(str.getBytes());
		stream.close();
		socket.close();
	}
	//服务端
	@Test
	public void server() throws Exception {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket(9898);
		Socket accept = serverSocket.accept();
		InputStream in = accept.getInputStream();
		byte[] b=new byte[1024];
		int len=0;
		while ((len=in.read(b))!=-1) {
			System.out.println(new String(b, 0, len));
		}
		in.close();
		accept.close();
		serverSocket.close();
	}
	
	
	//客户端2
	@Test
	public void clien2() throws Exception {
		//创建通道
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 9898));
		//创建通道
		FileChannel fileChannel = FileChannel.open(Paths.get("./src/1.png"), StandardOpenOption.READ);
		
		fileChannel.transferTo(0, fileChannel.size(), socketChannel);
		socketChannel.close();
		fileChannel.close();

	}
	//服务端2
	@Test
	public void server2() throws Exception {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(9898));
		SocketChannel socketChannel = serverSocketChannel.accept();

		FileChannel fileChannel = FileChannel.open(Paths.get("./src/7.png"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		while (socketChannel.read(byteBuffer)!=-1) {
			byteBuffer.flip();
			fileChannel.write(byteBuffer);
			byteBuffer.clear();
		}
		fileChannel.close();
		
		socketChannel.close();
		
		serverSocketChannel.close();
	}
	
	
	
	
}
