package zhou.program.string;

/**
 * 如果一串字符如"aaaabbc 中国1512"要分别统计英文字符的数量， 中文字符的数量， 和数字
 * 字符的数量，假设字符中没有中文字符、英文字符、数字字符之外的其他特殊字符
 * @author Zhou Yibing
 *
 */
public class CharacterSum {

	public static void main(String[] args) {

		String str="aaaabbc中国1512";
		statisticChar(str);		
	}

	/**
	 * 统计字符串
	 * @param str 待统计的字符串
	 */
	private static void statisticChar(String str) {
		char c;
		int digitCount = 0;//数字字符总数
		int englishCount = 0;//英文字符总数
		int chineseCount = 0;//中文字符总数
		
		for(int i=0;i<str.length();i++){
			c=str.charAt(i);
			if(c>='0'&&c<='9'){
				digitCount++;
			}else if((c>='a'&&c<='z')||(c>='A'&&c<='Z')){
				englishCount++;
			}else{
				chineseCount++;
			}
		}
		
		System.out.println("数字字符总数:"+digitCount);
		System.out.println("英文字符总数:"+englishCount);
		System.out.println("中文字符总数:"+chineseCount);
	}

}
