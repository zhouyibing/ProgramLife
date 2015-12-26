package zhou.base.jmx;

/**
 * 要管理Hello则必须创建一个相应MBean，包含在MBean中方法都将是可以被管理的。MBean起名是有规范的，就是原类名后加上MBean字样 
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
public interface HelloWorldMBean {

	public String getName();
	
	public void setName(String name);
	
	public void printHello();
	
	public void printHello(String name);
}
