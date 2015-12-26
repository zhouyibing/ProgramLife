package zhou.spring.beans;

import org.springframework.beans.factory.FactoryBean;

public class SimpleFactoryBean implements FactoryBean<ISimpleBean>{

	@Override
	public ISimpleBean getObject() throws Exception {
		// TODO Auto-generated method stub
		ISimpleBean bean = new SimpleBean();
		bean.setProperty1("aaaa");
		return bean;
	}

	@Override
	public Class<?> getObjectType() {
		return ISimpleBean.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
