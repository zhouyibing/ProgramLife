package zhou.base.core;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Created by Zhou Yibing on 2015/10/15.
 *
 * Java内省机制
 */
public class IntrospectorTest {

    public static void main(String[] args){
        SimpleBean bean = new SimpleBean("a","b");
        try {
            getProperty(bean,"p2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getProperty(SimpleBean bean,String propertyName) throws Exception{
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for(PropertyDescriptor propertyDescriptor:descriptors){
            if(propertyDescriptor.getName().equals(propertyName)){
               System.out.println( propertyDescriptor.getReadMethod().invoke(bean));
            }
        }
    }

    static class SimpleBean{
        private String p1;
        private String p2;

        public SimpleBean(String p1, String p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public String getP1() {
            return p1;
        }

        public void setP1(String p1) {
            this.p1 = p1;
        }

        public String getP2() {
            return p2;
        }

        public void setP2(String p2) {
            this.p2 = p2;
        }
    }

}
