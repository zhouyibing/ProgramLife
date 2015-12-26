package zhou.jta;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.UserTransaction;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

public class TransactionTest {
	public static void main(String[] args) {
		
	}
	
	 public static void transferAccount() { 
			 UserTransaction userTx = null; 
			 Connection connA = null; 
			 Statement stmtA = null; 
					
			 Connection connB = null; 
			 Statement stmtB = null; 
	    
			 try{ 
			       // 获得 Transaction 管理对象
				 Context ctx = new InitialContext();
				 userTx = (UserTransaction)ctx.lookup("java:comp/UserTransaction"); 
				 // 从数据库 A 中取得数据库连接
				 XADataSource xaDs1 = getDataSource("jdbc:mysql://192.168.1.254:3306/stopt?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull",
							"stopt","stopt");
				 connA = getXAConnection(xaDs1).getConnection(); 
				
				 // 从数据库 B 中取得数据库连接
				 XADataSource xaDs2 = getDataSource("jdbc:mysql://192.168.1.211:8066/stopt?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull",
						 "sto","123");
				 connB = getXAConnection(xaDs2).getConnection(); 
	      
	             // 启动事务
				 userTx.begin();
				
				 // 将 A 账户中的金额减少 500 
				 stmtA = connA.createStatement(); 
				 stmtA.execute("insert into t_opt_ship_source(waybill_no,source_org_code,source_org_ca_code) values('1354899846','322000','322000')");
				
				 // 将 B 账户中的金额增加 500 
				 stmtB = connB.createStatement(); 
				 stmtB.execute("insert into t_opt_ship_source(waybill_no,source_org_code,source_org_ca_code) values('1354899846','322000','322000')");
				
				 // 提交事务
				 userTx.commit();
				 // 事务提交：转账的两步操作同时成功（数据库 A 和数据库 B 中的数据被同时更新）
			 } catch(SQLException sqle){ 
				
				 try{ 
			  	       // 发生异常，回滚在本事务中的操纵
	                  userTx.rollback();
					 // 事务回滚：转账的两步操作完全撤销 
					 //( 数据库 A 和数据库 B 中的数据更新被同时撤销）
	                  stmtA.close(); 
	                  stmtB.close(); 
	                  connA.close(); 
	                  connB.close(); 
				 }catch(Exception ignore){ 
					
				 } 
				 sqle.printStackTrace(); 
				
			 } catch(Exception ne){ 
				 ne.printStackTrace(); 
			 } 
		 }
		 
		private static XAConnection getXAConnection(XADataSource dataSource) {
			XAConnection xaConn = null;
			try {
				xaConn = dataSource.getXAConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return xaConn;
		}
		
		private static Connection getConnection(XAConnection XAConn){
			Connection conn = null;
			try {
				conn = XAConn.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conn;
		}

		public static void closeConnection(Connection conn){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		private static XADataSource getDataSource(String url,String user,String password) {
			MysqlXADataSource dataSource = new MysqlXADataSource();
			dataSource.setUrl(url);
			dataSource.setUser(user);
			dataSource.setPassword(password);
			return dataSource;
		}
}

