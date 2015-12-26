package zhou.program.db;

import java.sql.*;

/**
 * Created by Zhou Yibing on 2015/11/27.
 */
public class Connect2Fabric {
    public static void main(String[] args){
        //testNormarMysql();
        testMysqlFabric();
    }

    public static void testNormarMysql(){
        Connection con = null;
        PreparedStatement stmt =null;
        try {
            //连接mysql fabric数据库
            Class.forName("com.mysql.jdbc.Driver");//FabricMySQLDriver;com.mysql.jdbc.Driver
            con = DriverManager.getConnection("jdbc:mysql://192.168.80.142:3306/test?user=root&password=123456");
            String sql = "insert into user(id,name,sex,age) values(?,?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1,1002);
            stmt.setString(2,"aaba");
            stmt.setInt(3,1);
            stmt.setInt(4,23);
            stmt.executeUpdate();
            //statement.executeQuery(sql);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(null!=con)
                    con.close();
                if(null!=stmt)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void testMysqlFabric(){
        Connection con = null;
        PreparedStatement stmt =null;
        try {
            //连接mysql fabric数据库
            Class.forName("com.mysql.fabric.jdbc.FabricMySQLDriver");//FabricMySQLDriver;com.mysql.jdbc.Driver
            con = DriverManager.getConnection("jdbc:mysql:fabric://192.168.80.142:32274/test?fabricServerGroup=my_group&fabricUsername=admin&fabricPassword=fabric");
            String sql = "insert into user(id,name,sex,age) values(?,?,?,?) ";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1,12);
            stmt.setString(2,"aaba");
            stmt.setInt(3,1);
            stmt.setInt(4,23);
            stmt.executeUpdate();
            //statement.executeQuery(sql);

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null!=con)
                    con.close();
                if(null!=stmt)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
