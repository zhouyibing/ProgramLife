package zhou.base.jmx;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

//import com.sun.jdmk.comm.HtmlAdaptorServer;

/**
 *  该类是一个Agent类，说明： 先创建了一个MBeanServer，用来做MBean的容器 
 *  将Hello这个类注入到MBeanServer中，注入需要创建一个ObjectName类 
 *  创建一个AdaptorServer，这个类将决定MBean的管理界面，这里用最普通的Html型界面。 
 *  AdaptorServer其实也是一个MBean。 
 *  zhouyibing:name=HelloWorld的名字是有一定规则的，格式为：“域名:name=MBean名称”， 
 *  域名和MBean名称都可以任意取。
 * @Description:
 *
 * @author yibing Zhou
 *
 * @date 2015-4-30
 *
 * @Modified History: 
 *
 ***
 */
public class TestHelloWorld {

	public static void main(String[] args) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException {
		 //创建一个MBeanServer，用来做MBean的容器  
        MBeanServer server = MBeanServerFactory.createMBeanServer();  
        //将Hello这个类注入到MBeanServer中，注入需要创建一个ObjectName类  
        ObjectName helloName = new ObjectName("zhouyibing:name=HelloWorld");  
          
        server.registerMBean(new HelloWorld(), helloName);  
        ObjectName adapterName = new ObjectName(  
                "HelloAgent:name=htmladapter,port=6666");  
        //创建一个AdaptorServer，这个类将决定MBean的管理界面，这里用最普通的Html型界面  
       /* HtmlAdaptorServer adapter = new HtmlAdaptorServer();
        adapter.setPort(8080);
        server.registerMBean(adapter, adapterName);
        adapter.start();
        System.out.println("start.....");  */
	}
}
