package zhou.program.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * jdbc连接示例
 * @author Zhou Yibing
 *
 */
public class JdbcConnection {

	public static void main(String[] args){
		try {
			//连接mysql数据库
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test","root","root");
			//连接oracle数据库
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection oracleCon = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:zhou", "scott","tiger");
			String sql = "";
			PreparedStatement stmt = con.prepareStatement(sql);
			Statement statement = con.createStatement();
			statement.executeQuery(sql);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
