package zhou.base.core;

public class NestClassInvoke {

	public void main(String[] args){
		NestedClass classNestedClass=new NestedClass();
		NestedClass.cls2 cls2=classNestedClass.new cls2();
		NestedClass.cls3 cls21=new NestedClass.cls3();
	}
}
