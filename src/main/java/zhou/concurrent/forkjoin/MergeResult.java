package zhou.concurrent.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 将这个问题拆分成多个子任务，并使用Fork／Join框架来执行这些子任务。
 * 执行完成后，原始任务获取到由所有这些子任务产生的结果，合并这些结果，返回最终的结果。
 * @author zhou
 *
 */
public class MergeResult {

	public static void main(String[] args) {
		Document mock=new Document();
		String[][] document=mock.generateDocument(100 ,1000, "the");
		DocumentTask task=new DocumentTask(document, 0, 100, "the");
		ForkJoinPool pool=new ForkJoinPool();
		pool.execute(task);
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n",pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n",pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n",pool.
			getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n",pool.getStealCount());
			System.out.printf("******************************************\n");
			try {
			TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			} while (!task.isDone());
		pool.shutdown();
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
		try {
			System.out.printf("Main: The word appears %d in the document",task.get());
			} catch (InterruptedException e) {
			e.printStackTrace();
			}catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		}

	}

	 class Document{
		private String words[]={"the","hello","goodbye","packt", "java","thread","pool","random","class","main"};
		public String[][] generateDocument(int numLines, int numWords,String word){
			int counter=0;
			String document[][]=new String[numLines][numWords];
			Random random=new Random();
			for (int i=0; i<numLines; i++){
				for (int j=0; j<numWords; j++) {
					int index=random.nextInt(words.length);
					document[i][j]=words[index];
					if (document[i][j].equals(word)){
					counter++;
				}
				}
			}
			System.out.println("DocumentMock: The word appears "+counter+" times in the document");
			return document;
		}
	}
	
	 class DocumentTask extends RecursiveTask<Integer>{

		private static final long serialVersionUID = 1L;
		private String[][] document;//文档二维数组
		private int start,end;//表示开始行和结束行
		private String word;//需要查找的单词
		
		public DocumentTask(String[][] document, int start, int end, String word) {
			this.document = document;
			this.start = start;
			this.end = end;
			this.word = word;
		}

		@Override
		protected Integer compute() {
			int result = 0;
			if(end-start<10){
				result = processLines(document,start,end,word);
			}else{
				//分割任务
				int mid = (end+start)/2;
			    DocumentTask documentTask1 = new DocumentTask(document, start, mid+1, word);
			    DocumentTask documentTask2 = new DocumentTask(document, mid+1,end, word);
			    invokeAll(documentTask1, documentTask2);
			    //将计算结果相加
			    try {
					result=groupResults(documentTask1.get(),documentTask2.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		private int groupResults(Integer number1, Integer number2) {
			Integer result;
			result=number1+number2;
			return result;
		}

		private int processLines(String[][] document, int start, int end,
				String word) {
			List<LineTask> tasks=new ArrayList<LineTask>();
			for (int i=start; i<end; i++){
			LineTask task=new LineTask(document[i], 0, document[i].length, word);
			tasks.add(task);
		  }
			invokeAll(tasks);
			//合计这些任务返回的值，并返回结果。
			int result=0;
			for (int i=0; i<tasks.size(); i++) {
			LineTask task=tasks.get(i);
			try {
			result=result+task.get();
			} catch (InterruptedException e) {
			e.printStackTrace();
			}catch (ExecutionException e1){
				e1.printStackTrace();
			}
			}
			return new Integer(result);
	}
	}
	
	  class LineTask extends RecursiveTask<Integer>{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String line[];
		private int start, end;
		private String word;

		public LineTask(String[] line, int start, int end, String word) {
			this.line = line;
			this.start = start;
			this.end = end;
			this.word = word;
		}

		@Override
		protected Integer compute() {
			Integer result=null;
			if (end-start<100) {
				result=count(line, start, end, word);
			} else {
				int mid=(start+end)/2;
				LineTask task1=new LineTask(line, start, mid+1, word);
				LineTask task2=new LineTask(line, mid+1, end, word);
				invokeAll(task1, task2);
				try {
					result=groupResults(task1.get(),task2.get());
					} catch (InterruptedException  e) {
					e.printStackTrace();
					} catch (ExecutionException e) {
					e.printStackTrace();
				}
					}
				return result;
		}

		private Integer groupResults(Integer number1, Integer number2) {
			Integer result;
			result=number1+number2;
			return result;
		}

		private Integer count(String[] line, int start, int end,String word) {
			int counter;
			counter=0;
			for (int i=start; i<end; i++){
			if (line[i].equals(word)){
			counter++;
			}
			}
			try {
				Thread.sleep(10);
				} catch (InterruptedException e) {
				e.printStackTrace();
				}
			return counter;
		}
	}
