
package jp.co.csj.tools.utils.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcConnectionManager {

	/**
	 * @Field: pool 数据库连接池
	 */
	private JdbcConnectionsPool pool ;
	
	public JdbcConnectionManager(String propertiesPath) {
		 pool = new JdbcConnectionsPool(propertiesPath);
	}

	/**
	 * @Method: getConnection
	 * @Description: 从数据库连接池中获取数据库连接对象
	 * @return Connection数据库连接对象
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return pool.getConnection();
	}

	/**
	 * @Method: release
	 * @Description: 释放资源，
	 *               释放的资源包括Connection数据库连接对象，负责执行SQL命令的Statement对象，存储查询结果的ResultSet对象
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public void release(Connection conn, Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				// 关闭存储查询结果的ResultSet对象
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (st != null) {
			try {
				// 关闭负责执行SQL命令的Statement对象
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				// 关闭Connection数据库连接对象
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}