package zhou.base.thread;

import java.lang.Thread.UncaughtExceptionHandler;
/**
 *当线程抛出一个未捕获的异常时，jvm将为异常寻找以下三种可能的处理器
 *首先，它查找线程对象的未捕获异常处理器，如果找不到，JVM继续查找线程对象所在线程租的未捕获异常处理器。
 *如果还是找不到，jvm将继续查找默认的未捕获异常处理器（setDefaultUncaughtExceptionHandler()）
 *如果没有一个处理器存在，jvm则将堆栈异常记录打印到控制台，并退出程序
 ***
 */
public class TestThreadException {

	public static void main(String[] args) {

		Thread t = new SubThread();
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("uncaughtExceptionHandler:"+t);
			}
		});
		t.start();
	}

    static class SubThread extends Thread{

		@Override
		public void run() {
			for(int i = 0;i<10;i++){
				if(i==4)
					Integer.parseInt("aa");
				System.out.println(this.getName()+":"+i);
			}
		}
    }
    
}
