package zhou.base.rpc.protocol;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Invocation类的实例需要进行网络传输，所以要实现序列化接口
 * 主要是定义rpc调用过程中使用的数据结构
 *
 ***
 */
public class Invocation implements Serializable{
	private static final long serialVersionUID = 1L;

	private Class interfaces;
	private Method method;
	private Object[] params;
	private Object result;
	
	public Class getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(Class interfaces) {
		this.interfaces = interfaces;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return interfaces.getName()+"."+method.getMethodName()+"("+Arrays.toString(params)+")";
	}
}
