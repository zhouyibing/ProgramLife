package framework.guava.utilities;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;

public class TestString {

	@Test
	public void testString(){
		System.out.println(Strings.padEnd("foo",6,'x'));
		System.out.println(Strings.padStart("foo",6,'x'));
		System.out.println(Strings.repeat("bc", 3));
	}
	
	@Test
	public void testCharsets(){
		//It's worth noting that as of Java 7, there is a StandardCharsets class that also
		//provides static final definitions to the six standard character sets supported
		//on the Java platform
		
		byte[] bytes2 = "foobarbaz".getBytes(Charsets.UTF_8);
	}
	
	@Test
	public void testRemoveWhiteSpace(){
	String tabsAndSpaces = "String            with                     spaces             tabs";
	String expected = "String with spaces and tabs";
	String scrubbed = CharMatcher.WHITESPACE.collapseFrom(tabsAndSpaces,' ');
	System.out.println(scrubbed);
	
	String stringWithLinebreaks="sdf\ndsfds\rfds";
	System.out.println(CharMatcher.BREAKING_WHITESPACE.replaceFrom(stringWithLinebreaks,' '));
	
	}
	
	@Test
	public void testTrimRemoveWhiteSpace(){
	String tabsAndSpaces ="           String with       spaces              tabs                  ";
	String expected = "String with spaces and tabs";
	String scrubbed = CharMatcher.WHITESPACE.trimAndCollapseFrom(tabsAndSpaces,' ');
	System.out.println(scrubbed);
	}
	
	@Test
	public void testRetainFrom(){
	String lettersAndNumbers = "foo989yxbar234";
	String expected = "989234";
	String retained = CharMatcher.JAVA_DIGIT.retainFrom(lettersAndNumbers);
	String strRetained = CharMatcher.JAVA_LETTER.retainFrom(lettersAndNumbers);
	CharMatcher cm = CharMatcher.JAVA_DIGIT.or(CharMatcher.WHITESPACE);
	System.out.println(cm.collapseFrom("aa s          dsf", '$'));
	System.out.println(cm.retainFrom("aa s          d12sf"));
	System.out.println(retained);
	System.out.println(strRetained);
	}
}
