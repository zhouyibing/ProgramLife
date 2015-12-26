package zhou.base.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BlockRead {

	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream in = new FileInputStream(new File("e:\\eeee.xlsx"));
		FileThread th = new FileThread();
		th.setInputStream(in);
		FileThread th2 = new FileThread();
		th2.setInputStream(in);
		Thread fileThread = new Thread(th);
		Thread fileThread2 = new Thread(th2);
		fileThread.setName("thread1");
		fileThread.start();
		fileThread2.setName("thread2");
		fileThread2.start();
	}

	static class FileThread implements Runnable{
		private InputStream in = null;
		public void setInputStream(InputStream in){
			this.in=in;
		}
		
		@Override
		public void run() {
			try {
				in.read();
				System.out.println(Thread.currentThread().getName()+":begin to read file...");
				Thread.sleep(30000);
				in.close();
				System.out.println(Thread.currentThread().getName()+":end...");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
