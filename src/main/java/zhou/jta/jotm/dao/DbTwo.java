package zhou.jta.jotm.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DbTwo  extends JdbcDaoSupport{
	public void add (){
		try {
			System.out.println("------tb2 add--------");
			String sqlString = "insert into t_pda_resolve_fail2(id,resolve_content,exception_trace) values(?,?,?)";
			this.getJdbcTemplate().update(sqlString.toString(),new Object[]{1234,"1354899846","322000"});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
}
