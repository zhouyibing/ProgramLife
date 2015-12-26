package zhou.base.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 字符流于字节流之间的转换
 * @author Zhou Yibing
 *
 */

public class FileTest {

	public static void main(String[] args) {

		String str="斯蒂芬森等e";
		/*try {
			FileOutputStream fileOut=new FileOutputStream("d:\\1.txt");
			fileOut.write(str.getBytes("utf-8"));
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		/*try {
			PrintWriter writer=new PrintWriter("d:\\2.txt");
			writer.write(str);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		
		/*try {
			FileReader reader=new FileReader("d:\\1.txt");
			char[] chars=new char[1024];
			reader.read(chars);
			String readedStr=new String(chars);
			System.out.println(readedStr);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream("d:/2.txt")));
			System.out.println(reader.readLine());
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
