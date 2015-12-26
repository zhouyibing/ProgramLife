package zhou.program.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

/**
 * java调用存储过程
 * @author Zhou Yibing
 *
 */
public class JavaCallProcedure {

	public static void main(String[] args) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:zhou", "scott","tiger");
			//1.创建callableStatement
			CallableStatement callStatement = con.prepareCall("{call insert_emp(?,?,?)}");
			//2.设置参数
			callStatement.setString(1,"aa");
			callStatement.setInt(2,10);
			callStatement.registerOutParameter(3,Types.NUMERIC);
			callStatement.execute();
			System.out.println(callStatement.getInt(3));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
