package zhou.spring.beans;

public class SimpleBean implements ISimpleBean{

	private String property1;
	private int property2;
	private double property3;
	public String getProperty1() {
		return property1;
	}
	public void setProperty1(String property1) {
		this.property1 = property1;
	}
	public int getProperty2() {
		return property2;
	}
	public void setProperty2(int property2) {
		this.property2 = property2;
	}
	public double getProperty3() {
		return property3;
	}
	public void setProperty3(double property3) {
		this.property3 = property3;
	}
}
