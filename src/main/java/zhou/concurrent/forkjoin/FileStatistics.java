package zhou.concurrent.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 统计指定文件类型的个数，
 * 当采用同步方式，调用这些方法（比如，invokeAll()方法）时，任务被挂起，
 * 直到任务被发送到Fork/Join线程池中执行完成。这种方式允许ForkJoinPool类采用工作窃取算法（Work-StealingAlgorithm）来分配一个新任务给在执行休眠任务的工作者线程（WorkerThread）
 * 
 * 当采用异步方法（比如，fork()方法）时，任务将继续执行，
 * 因此ForkJoinPool类无法使用工作窃取算法来提升应用程序的性能。在这个示例中，只有调用join()或get()方法来等待任务的结束时，ForkJoinPool类才可以使用工作窃取算法。
 * 
 * get()方法和join()方法还存在两个主要的区别：

join()方法不能被中断，如果中断调用join()方法的线程，方法将抛出InterruptedException异常；
如果任务抛出任何运行时异常，那么 get()方法将返回ExecutionException异常，但是join()方法将返回RuntimeException异常。
 * @author zhou
 *
 */
public class FileStatistics {

	public static void main(String[] args) {

		ForkJoinPool pool = new ForkJoinPool();
		FolderProcessor spring = new FolderProcessor("G:\\快盘\\SSH\\spring-framework-3.0.4.RELEASE", ".java");
		pool.execute(spring);
		FolderProcessor windows = new FolderProcessor("C:\\Windows", ".dll");
		pool.execute(windows);
		FolderProcessor asdk = new FolderProcessor("E:\\ASDK", ".html");
		pool.execute(asdk);
		
		FolderProcessor normalFile = new FolderProcessor("D:\\ashk_pic.bmp", ".bmp");
		pool.execute(normalFile);
		
		FolderProcessor zl = new FolderProcessor("E:\\资料", ".flv");
		pool.execute(zl);
		
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n",pool.
			getParallelism());
			System.out.printf("Main: Active Threads: %d\n",pool.
			getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n",pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n",pool.getStealCount());
			System.out.printf("******************************************\n");
			try {
			TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			} while ((!spring.isDone())||(!windows.isDone())||(!asdk.isDone())||(!normalFile.isDone())||(!zl.isDone()));
		List<String> results;
		results=spring.join();//一旦主任务处理完指定文件夹里的所有内容，它将调用join()方法等待发送到线程池中的所有子任务执行完成
		System.out.printf("Spring: %d files like .java found.\n",results.size());
		results=windows.join();
		System.out.printf("Windows: %d files like .dll found.\n",results.size());
		results=asdk.join();
		System.out.printf("ASDK: %d files like .html found.\n",results.size());
		results=normalFile.join();
		System.out.printf("normalFile: %d files like .bmp found.\n",results.size());
		results=zl.join();
		System.out.printf("zl: %d files like .flv found.\n",results.size());

	}

	static class FolderProcessor extends RecursiveTask<List<String>>{

		private String path;
		private String extension;
		
		public FolderProcessor(String path, String extension) {
			this.path = path;
			this.extension = extension;
		}

		@Override
		protected List<String> compute() {
			List<String> matchedFiles = new ArrayList<String>();
			File file = new File(path);
			List<FolderProcessor> tasks=new ArrayList<FolderProcessor>();
			if(!file.isDirectory()){
				if(match(file.getName(), extension))
				matchedFiles.add(file.getAbsolutePath());
			}else{
				File[] childFiles = file.listFiles();
				if(null!=childFiles)
				for(int i =0;i<childFiles.length;i++){
					if(childFiles[i].isDirectory()){
						FolderProcessor folderProcessor = new FolderProcessor(childFiles[i].getAbsolutePath(), extension);
						folderProcessor.fork();//对于文件夹中的每一个元素，如果它是子文件夹，就创建一个新的FolderProcessor对象，然后调用fork()方法采用异步方式来执行它;fork()方法会立即返回，因此，主任务可以继续处理文件夹里的其他内容
						tasks.add(folderProcessor);
					}else{
						if(match(childFiles[i].getName(), extension))
							matchedFiles.add(childFiles[i].getAbsolutePath());
					}
				}
			}
			if (tasks.size()>50) {
				System.out.printf("%s: %d tasks ran.\n",file.
				getAbsolutePath(),tasks.size());
			}
			for (FolderProcessor item: tasks) {
				matchedFiles.addAll(item.join());
			 }
			return matchedFiles;
		}

		public boolean match(String fileName,String ext){
			return fileName.endsWith(ext);
		}
	}
}
