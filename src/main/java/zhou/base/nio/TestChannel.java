package zhou.base.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 *任何时候读取数据，都不是直接从通道读取，而是从通道读取到缓冲区。所以使用NIO读取数据可以分为下面三个步骤： 
  1. 从FileInputStream获取Channel 
  2. 创建Buffer 
  3. 将数据从Channel读取到Buffer中
  
  使用NIO写入数据与读取数据的过程类似，同样数据不是直接写入通道，而是写入缓冲区，可以分为下面三个步骤： 
1. 从FileOutputStream获取Channel 
2. 创建Buffer 
3. 将数据从Channel写入到Buffer中
 *
 ***
 */
public class TestChannel {
	 static private final byte message[] = { 83, 111, 109, 101, 32,  
	        98, 121, 116, 101, 115, 46 };
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//testChannelRead();
		//testNormalRead();
		testChannelWrite();
	}
	
	static void testChannelWrite() throws IOException{
		FileOutputStream fout = new FileOutputStream( "src\\zhou\\base\\nio\\test.txt" );  
        
        FileChannel fc = fout.getChannel();  
          
        ByteBuffer buffer = ByteBuffer.allocate( 1024 );  
          
        for (int i=0; i<message.length; ++i) {  
            buffer.put( message[i] );  
        }  
          
        buffer.flip();  
          
        fc.write( buffer );  
          
        fout.close();  
	}
	
	static void testChannelRead() throws IOException{
		FileInputStream in = new FileInputStream("src\\zhou\\base\\nio\\test.txt");
		//获取通道
		FileChannel channel = in.getChannel();
		//创建缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		//读取数据到缓冲区
		channel.read(buffer);
		//重设缓冲区
		buffer.flip();
		while(buffer.hasRemaining()){
			byte b = buffer.get();
			System.out.print((char)b);
		}
		channel.close();
	}

	static void testNormalRead() throws IOException{
		FileInputStream in = new FileInputStream("src\\zhou\\base\\nio\\test.txt");
		int b;
		while((b = in.read())!=-1){
			System.out.println((char)b);
		}
		in.read();
	}
}
