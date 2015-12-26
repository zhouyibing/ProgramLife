package zhou.jta.jotm.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


public class DbOne  extends JdbcDaoSupport{
	
	public void add (){
		try {
			System.out.println("------tb1 add--------");
			String sqlString = "insert into t_opt_ship_source(waybill_no,source_org_code,source_org_ca_code) values(?,?,?)";
			this.getJdbcTemplate().update(sqlString.toString(),new Object[]{"1354899846","322000","322000"});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
