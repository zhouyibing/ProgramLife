package zhou.spring.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zhou.spring.beans.ISimpleBean;

public class TestBeanFactoryPostProcessor {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("zhou/spring/applicationContext.xml"); 
		ISimpleBean simpleBean = (ISimpleBean)context.getBean("simpleBean");
		System.out.println(simpleBean.getProperty1());
	}

	static class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor{

		//通过beanFactory可以获取bean的示例或定义等。同时可以修改bean的属性，这是和BeanPostProcessor最大的区别;
		//BeanFactoryPostProcessor的回调比BeanPostProcessor要早
		@Override
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
				throws BeansException {
			BeanDefinition bd = beanFactory.getBeanDefinition("simpleBean");  
			MutablePropertyValues mpv =  bd.getPropertyValues();  
			if(mpv.contains("property1"))  {
				mpv.addPropertyValue("property1", "change orignal property value "+mpv.getPropertyValue("property1").getValue()+" by CustomBeanFactoryPostProcessor");  
			}
		}
	}
}
