package zhou.program.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 编写一个程序，将 d:\java 目录下的所有.java 文件复制到 d:\jad 目录下， 并
 * 将原来文件的扩展名从.java 改为.jad
 * @author Zhou Yibing
 *
 */
public class FileCopy {

	public static void main(String[] args) {
        try {
			copyAndRename("d:\\java","d:\\jad","java","jad");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制目录中的文件到另一个目录
	 * @param sourceDir 源目录
	 * @param destinationDir 目标目录
	 * @param sourceExtension 源扩展名
	 * @param destinationExtension 目标扩展名
	 * @throws IOException 
	 */
	private static void copyAndRename(String sourceDir, String destinationDir, String sourceExtension, String destinationExtension) throws IOException {
		File source = new File(sourceDir);
		if(!source.exists()||!source.isDirectory()){
			System.out.println("目录不存在");
			return;
		}
		File[] childFiles=source.listFiles(new JavaFileFilter());
		FileOutputStream fileWriter;
		FileInputStream fileReader;
		
		for(File file:childFiles){
			fileReader=new FileInputStream(file);
			fileWriter=new FileOutputStream(new File(destinationDir,file.getName().replaceAll(sourceExtension,destinationExtension)));
			//复制文件内容
			copyFile(fileReader,fileWriter);
		}
	}

	private static void copyFile(FileInputStream fileReader,
			FileOutputStream fileWriter) throws IOException {
		int len=0;
		byte[] buff=new byte[1024];
		while((len=fileReader.read(buff))!=-1){
			fileWriter.write(buff,0,len);
		}
		fileReader.close();
		fileWriter.close();
	}

	static class JavaFileFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".java");
		}
		
	}
}
