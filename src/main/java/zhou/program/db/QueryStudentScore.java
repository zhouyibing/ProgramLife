package zhou.program.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryStudentScore {

	public static void main(String[] args) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			//con.setAutoCommit(false);
			//con.commit();
			//con.rollback();
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test","root","123456");
			pstmt = con.prepareStatement("select * from user");
			//pstmt.setString(1,"a");
			//pstmt.executeUpdate();
			//pstmt.executeBatch();
			rs = pstmt.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString("id")+","+rs.getString("name"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
				try {
					if(null != con)
					   con.close();
					if(null != pstmt)
					   pstmt.close();
					if(null != rs)
					   rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

}
