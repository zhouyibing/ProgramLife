package zhou.base.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 *
 ***
 */
public class TestBufferFeatures {

	public static void main(String[] args) throws IOException {
		//bufferSlice();
		//readOnlyBuffer();
		//directBuffer();
		mappedBuffer();
	}

	/**
	 * 在创建一个缓冲区对象时，会调用静态方法allocate()来指定缓冲区的容量，其实调用 allocate()相当于创建了一个指定大小的数组，并把它包装为缓冲区对象。或者我们也可以直接将一个现有的数组，包装为缓冲区对象
	 */
	static void bufferAllocate(){
		// 分配指定大小的缓冲区  
        ByteBuffer buffer1 = ByteBuffer.allocate(10);  
          
        // 包装一个现有的数组  
        byte array[] = new byte[10];  
        ByteBuffer buffer2 = ByteBuffer.wrap( array );  
	}
	
	/**
	 * 可以根据现有的缓冲区对象来创建一个子缓冲区，即在现有缓冲区上切出一片来作为一个新的缓冲区，但现有的缓冲区与创建的子缓冲区在底层数组层面上是数据共享的，
	 * 也就是说，子缓冲区相当于是现有缓冲区的一个视图窗口。调用slice()方法可以创建一个子缓冲区
	 */
	static void bufferSlice(){
		ByteBuffer buffer = ByteBuffer.allocate( 10 );  
         
        // 缓冲区中的数据0-9  
        for (int i=0; i<buffer.capacity(); ++i) {  
            buffer.put( (byte)i );  
        }  
          
        // 创建子缓冲区  
        buffer.position( 3 );  
        buffer.limit( 7 ); 
        ByteBuffer slice = buffer.slice();  
          
        // 改变子缓冲区的内容  
        for (int i=0; i<slice.capacity(); ++i) {  
            byte b = slice.get( i );  
            b *= 10;  
            slice.put( i, b );  
        }  
          
        buffer.position( 0 );  
        buffer.limit( buffer.capacity() );  
          
        while (buffer.remaining()>0) {  
            System.out.println( buffer.get() );  
        }  
   }
	
	/**
	 * 只读缓冲区非常简单，可以读取它们，但是不能向它们写入数据。可以通过调用缓冲区的asReadOnlyBuffer()方法，将任何常规缓冲区转 换为只读缓冲区，
	 * 这个方法返回一个与原缓冲区完全相同的缓冲区，并与原缓冲区共享数据，只不过它是只读的。如果原缓冲区的内容发生了变化，只读缓冲区的内容也随之发生变化
	 */
   static void readOnlyBuffer(){
	   ByteBuffer buffer = ByteBuffer.allocate( 10 );  
       
       // 缓冲区中的数据0-9  
       for (int i=0; i<buffer.capacity(); ++i) {  
           buffer.put( (byte)i );  
       }  
 
       // 创建只读缓冲区  
       ByteBuffer readonly = buffer.asReadOnlyBuffer();  
         
       // 改变原缓冲区的内容  
       for (int i=0; i<buffer.capacity(); ++i) {  
           byte b = buffer.get( i );  
           b *= 10;  
           buffer.put( i, b );  
       }  
       
       //源缓冲区数据的变更并不会影响只读缓存区的position,limit,所以要手动设置
       readonly.position(0);  
       readonly.limit(buffer.capacity());  
       //readonly.put(b);尝试修改只读缓冲区会抛出 ReadOnlyBufferException
       // 只读缓冲区的内容也随之改变  
       while (readonly.remaining()>0) {  
           System.out.println( readonly.get());  
       }  
   }
   
   /**
    * 直接缓冲区是为加快I/O速度，使用一种特殊方式为其分配内存的缓冲区，JDK文档中的描述为：给定一个直接字节缓冲区，
    * Java虚拟机将尽最大努 力直接对它执行本机I/O操作。也就是说，它会在每一次调用底层操作系统的本机I/O操作之前(或之后)，
    * 尝试避免将缓冲区的内容拷贝到一个中间缓冲区中 或者从一个中间缓冲区中拷贝数据。
    * 要分配直接缓冲区，需要调用allocateDirect()方法，而不是allocate()方法，使用方式与普通缓冲区并无区别，
    * @throws IOException
    */
   static void directBuffer() throws IOException{
	   String infile = "src\\zhou\\base\\nio\\test.txt";  
       FileInputStream fin = new FileInputStream( infile );  
       FileChannel fcin = fin.getChannel();  
         
       String outfile = String.format("src\\zhou\\base\\nio\\testCopy.txt");  
       FileOutputStream fout = new FileOutputStream( outfile );      
       FileChannel fcout = fout.getChannel();  
         
       // 使用allocateDirect，而不是allocate  
       ByteBuffer buffer = ByteBuffer.allocateDirect( 1024 );  
         
       while (true) {  
           buffer.clear();  
             
           int r = fcin.read( buffer );  
             
           if (r==-1) {  
               break;  
           }  
             
           buffer.flip();  
             
           fcout.write( buffer );  
       }
   }
   
   static private final int start = 0;
   static private final int size = 1024; 

   /**
    * 内存映射文件I/O是一种读和写文件数据的方法，它可以比常规的基于流或者基于通道的I/O快的多。内存映射文件I/O是通过使文件中的数据出现为 内存数组的内容来完成的，
    * 这其初听起来似乎不过就是将整个文件读到内存中，但是事实上并不是这样。一般来说，只有文件中实际读取或者写入的部分才会映射到内存中。
    * @throws IOException
    */
   static void mappedBuffer() throws IOException{
	   RandomAccessFile raf = new RandomAccessFile( "src\\zhou\\base\\nio\\test.txt", "rw" );  
       FileChannel fc = raf.getChannel();  
         
       MappedByteBuffer mbb = fc.map( FileChannel.MapMode.READ_WRITE,  
         start, size );  
         
       mbb.put( 0, (byte)97 );  
       mbb.put( 1023, (byte)122 );  
         
       raf.close();  
   }
}
