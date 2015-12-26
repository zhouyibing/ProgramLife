package zhou.program.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperate {

	public static void main(String[] args) throws IOException {

		BufferedReader aFile=new BufferedReader(new FileReader("src\\zhou\\program\\file\\a.txt"));
		BufferedReader bFile=new BufferedReader(new FileReader("src\\zhou\\program\\file\\b.txt"));
		BufferedWriter cFile=new BufferedWriter(new FileWriter("src\\zhou\\program\\file\\c.txt"));
		String[] separator=new String[]{"\n"," "};
		String aline = null;
		String bline = null;
		do{
			aline=aFile.readLine();
			bline=bFile.readLine();
			String[] aWords=getWords(aline,separator);
			String[] bWords=getWords(bline,separator);
			for(int i=0;i<aWords.length;i++){
				cFile.write(aWords[i],0, aWords[i].length());
				cFile.newLine();
				cFile.write(bWords[i],0, bWords[i].length());
				cFile.newLine();
			}
		}while(null==bline);
		cFile.flush();
		aFile.close();
		bFile.close();
	}

	public static String[] getWords(String originalStr,String[] separators){
		String regex="";
		for(String separator:separators)
			regex=regex+"|"+separator;
		String[] words=originalStr.split(regex);
		return words;
	}
}
