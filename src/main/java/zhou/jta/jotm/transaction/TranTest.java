package zhou.jta.jotm.transaction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhou.jta.jotm.dao.DbOne;
import zhou.jta.jotm.dao.DbTwo;



public class TranTest {
	
	private DbOne dbone = null;
	
	private DbTwo dbtwo = null;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(){
		dbone.add();
		dbtwo.add();
	}

	public DbOne getDbone() {
		return dbone;
	}

	public void setDbone(DbOne dbone) {
		this.dbone = dbone;
	}

	public DbTwo getDbtwo() {
		return dbtwo;
	}

	public void setDbtwo(DbTwo dbtwo) {
		this.dbtwo = dbtwo;
	}
	
	
}
