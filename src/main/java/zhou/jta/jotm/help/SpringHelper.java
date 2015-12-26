package zhou.jta.jotm.help;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringHelper {
	public static Object GetObjectWithSpringContext(String objName) {
		try {
			ApplicationContext springContext =new ClassPathXmlApplicationContext("\\zhou\\jta\\jotm\\AppConmon.xml");
			if (springContext.containsBean(objName)) {
				Object obj =springContext.getBean(objName);
				return obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		
	}
}