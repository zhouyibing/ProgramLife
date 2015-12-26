package zhou.jta;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

public class JTATest {

	public static void main(String[] args){
		XADataSource xaDs1 = JTATest.getDataSource("jdbc:mysql://192.168.1.254:3306/stopt?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull",
				"stopt","stopt");
		XAConnection xaConn1 = null;
		XAResource xaRes1 = null;
		Connection conn1 = null;
		Statement stmt1 = null;
		
		XADataSource xaDs2 = JTATest.getDataSource("jdbc:mysql://192.168.1.180:3309/stpda?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull",
				"stpda","stpda");
		XAConnection xaConn2 = null;
		XAResource xaRes2 = null;
		Connection conn2 = null;
		Statement stmt2 = null;
		
		int ret1 = 0;
		int ret2 = 0;
		
		Xid xid1 = new MyXid(100,new byte[]{0x01}, new byte[]{0x02});
		Xid xid2 = new MyXid(100,new byte[]{0x01}, new byte[]{0x03});
		
		try {
		xaConn1 = JTATest.getXAConnection(xaDs1);
		conn1 = JTATest.getConnection(xaConn1);
		stmt1 = conn1.createStatement();
		xaRes1 = xaConn1.getXAResource();
		
		xaConn2 = JTATest.getXAConnection(xaDs2);
		conn2 = JTATest.getConnection(xaConn2);
		stmt2 = conn2.createStatement();
		xaRes2 = xaConn2.getXAResource();
		
		/////////
		xaRes1.start(xid1, XAResource.TMNOFLAGS);
		stmt1.execute("insert into t_opt_ship_source(waybill_no,source_org_code,source_org_ca_code) values('1354899846','322000','322000')");
		xaRes1.end(xid1, XAResource.TMSUCCESS);
		
		//if the xaRes2,xaRes1 managed by the same resource manager
		//它将察看是否使用和第一个XA资源使用的是同一个资源管理程序。如果这是实例，它将加入在第一个XA连接上创建的第一个分支，而不是创建一个新的分支。稍后，这个事务分支使用XA资源来准备和提交。
		if(xaRes2.isSameRM(xaRes1)){
			xaRes2.start(xid1, XAResource.TMJOIN);
			stmt2.execute("insert into t_pda_resolve_fail2(id,resolve_content,exception_trace) values(1234,'1354899846','322000')");
			xaRes2.end(xid1, XAResource.TMSUCCESS);
		}else{
			xaRes2.start(xid2, XAResource.TMNOFLAGS);
			stmt2.execute("insert into t_pda_resolve_fail2(id,resolve_content,exception_trace) values(1234,'1354899846','322000')");
			xaRes2.end(xid2, XAResource.TMSUCCESS);
			ret2 = xaRes2.prepare(xid2);
		}
		ret1 = xaRes1.prepare(xid1);
		
		if(ret1==XAResource.XA_OK&&ret2==XAResource.XA_OK){
			xaRes1.commit(xid1, false);
			if (xaRes2.isSameRM(xaRes1)) {  
				xaRes2.commit(xid1, false);
             }else{ 
            	 xaRes2.commit(xid2, false);
             }
		}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				stmt1.close();
				stmt2.close();
				conn1.close();
				conn2.close();
				xaConn1.close();
				xaConn2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

	static class MyXid implements Xid
	{
	protected int formatId;
	protected byte[] gtrid;
	protected byte[] bqual;
	public MyXid()
	{
	}
	public MyXid(int formatId,byte[] gtrid,byte[] bqual)
	{
	this.formatId = formatId;
	this.gtrid = gtrid;
	this.bqual = bqual;
	}
	public int getFormatId()
	{
	return formatId;
	}
	public byte[] getBranchQualifier()
	{
	return bqual;
	}
	public byte[] getGlobalTransactionId()
	{
	return gtrid;
	}
	}
}
