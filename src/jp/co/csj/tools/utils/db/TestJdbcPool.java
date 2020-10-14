
package jp.co.csj.tools.utils.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.csj.tools.utils.log.CsjLog4j;


public class TestJdbcPool {

    public static void main(String[] args) throws SQLException {
        //实例化封装了有关数据库类方法类的对象
        JdbcConnectionManager jcp=new JdbcConnectionManager("/mysql_db.properties");
        //获得数据库连接对象
        Connection conn=jcp.getConnection();
        //下面代码是存储过程的调用
        String s="select * from emp";
        CallableStatement cst=conn.prepareCall(s);
        ResultSet rs=cst.executeQuery();

        List<Map<String,Object>> lst = new ArrayList<>();
        while(rs.next()) {
			// 各行の内容を格納するために、マップを初期化する
			Map<String,Object> map = new LinkedHashMap<>();

			for(int i = 1; i < 	rs.getMetaData().getColumnCount(); i++) {
				map.put(rs.getMetaData().getColumnLabel(i),rs.getObject(i));
			}

			// 当該行の内容を結果リストに格納する
			lst.add(map);
			CsjLog4j.logger.debug(map);
        }
        
        //关闭所有的数据库资源
        jcp.release(conn, cst, rs);
    }
}