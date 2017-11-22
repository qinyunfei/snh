package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class mynio {
	
	//复制文件 io 123
	@SuppressWarnings("resource")
	@Test
	public void name() throws Exception {
		FileInputStream in = new FileInputStream("./src/1.png");
		FileOutputStream out = new FileOutputStream("./src/2.png");
		byte[] b=new byte[1024];
		int len =0;
		while ((len=in.read(b))!=-1) {
			out.write(b);
		}
		in.close();
		out.close();
	}
	//复制文件 nio (非直接缓冲区)
	@Test
	public void name2() throws Exception {
		FileInputStream in = new FileInputStream("./src/1.png");
		FileOutputStream out = new FileOutputStream("./src/3.png");
		FileChannel inchannel = in.getChannel();
		FileChannel channel = out.getChannel();
		ByteBuffer allocate = ByteBuffer.allocate(1024);
		while (inchannel.read(allocate)!=-1) {
			allocate.flip();
			channel.write(allocate);
			allocate.clear();
		}
		inchannel.close();
		channel.close();
		in.close();
		out.close();
	}
	//复制文件 nio (直接缓冲区)
	@Test
	public void name3() throws Exception {
		FileChannel fileChannel = FileChannel.open(Paths.get("./src/1.png"), StandardOpenOption.READ);
		FileChannel open = FileChannel.open(Paths.get("./src/4.png"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		MappedByteBuffer buffer = fileChannel.map(MapMode.READ_ONLY,0,fileChannel.size());
		MappedByteBuffer map = open.map(MapMode.READ_WRITE, 0, fileChannel.size());
		byte[] b=new byte[buffer.limit()];
		buffer.get(b);
		map.put(b);
		fileChannel.close();
		open.close();
	}
	//复制文件 nio (直接缓冲区)
	@Test
	public void name4() throws Exception {
		FileChannel ch1 = FileChannel.open(Paths.get("./src/1.png"), StandardOpenOption.READ);
		FileChannel ch2 = FileChannel.open(Paths.get("./src/5.png"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		ch1.transferTo(0, ch1.size(), ch2);
		ch1.close();
		ch2.close();
	}
	
	@Test
	public void name5() throws Exception {
		SeekableByteChannel newByteChannel = Files.newByteChannel(Paths.get("./src/1.png"), StandardOpenOption.READ);
		SeekableByteChannel newByteChannel2 = Files.newByteChannel(Paths.get("./src/6.png"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		ByteBuffer allocate = ByteBuffer.allocate((int) newByteChannel.size());
		newByteChannel.read(allocate);
		allocate.flip();
		newByteChannel2.write(allocate);
		newByteChannel.close();
		newByteChannel2.close();
	
	}

}
