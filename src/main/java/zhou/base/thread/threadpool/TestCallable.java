package zhou.base.thread.threadpool;

import java.util.concurrent.Callable;

public class TestCallable {

	public static void main(String[] args) {
		try {
			new ThreadCallable().call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ThreadCallable implements Callable<String>{

		@Override
		public String call() throws Exception {
			System.out.println("dddd");
			return null;
		}
		
	}
}
