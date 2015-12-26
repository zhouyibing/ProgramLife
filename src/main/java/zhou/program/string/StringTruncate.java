package zhou.program.string;

/**
 * 编写一个截取字符串的函数，输入为一个字符串和字节数，输出为按字节截
 * 取的字符串， 但要保证汉字不被截取半个， 如“我 ABC”， 4， 应该截取“我 AB”，
 * 输入“我 ABC 汉 DEF”，6，应该输出“我 ABC”，而不是“我 ABC+汉的半个”
 * @author Zhou Yibing
 *
 */
public class StringTruncate {

	public static void main(String[] args) {

		String originalStr="水电d费c水电dsdfs费是的";
		String truncatedStr=truncate(originalStr,5);
		System.out.println(truncatedStr);
	}

	/**
	 * 截取字符串
	 * @param originalStr 原始字符串
	 * @param size 需要截取的字节数
	 * @return
	 */
	private static String truncate(String originalStr, int size) {
		byte[] strBytes=originalStr.getBytes();
		int num = 0;
		boolean chineseHalfFirst=false;
		for(int i=0;i<size;i++){
			if(strBytes[i]<0&&!chineseHalfFirst){
				chineseHalfFirst=true;
			}else{
				chineseHalfFirst=false;
				num++;
			}
		}
		return originalStr.substring(0, num);
	}

}
