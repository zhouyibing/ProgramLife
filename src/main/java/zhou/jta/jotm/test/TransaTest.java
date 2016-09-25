package zhou.jta.jotm.test;


import org.junit.Test;

import zhou.jta.jotm.transaction.TranTest;
import zhou.jta.jotm.help.SpringHelper;

public class TransaTest {
	
	@Test
	public void test(){
		TranTest tran = (TranTest)SpringHelper.GetObjectWithSpringContext("trantest");
		tran.add();
	}
}
