package jp.co.csj.tools.utils.db;

public class DbConst {

	public static final String DRIVER_CLASS_ORACLE = "oracle.jdbc.driver.OracleDriver";
	public static final String DRIVER_CLASS_POSTGRE = "org.postgresql.Driver";
	public static final String DRIVER_CLASS_MYSQL = "com.mysql.jdbc.Driver";
	public static final String DRIVER_CLASS_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String DRIVER_CLASS_SQLSERVER_2000 = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static final String DRIVER_CLASS_DB2 = "com.ibm.db2.jcc.DB2Driver";
	public static final String DRIVER_CLASS_SYBASE = "com.sybase.jdbc4.jdbc.SybDriver";
	public static final String DRIVER_CLASS_SQLITE = "org.sqlite.JDBC";

	public static final String SQL_DATE_ORALCE="select to_char(sysdate,'YYYYMMDD') STR_DATE from dual";
	public static final String SQL_DATE_POSTGRE="select to_char( CURRENT_DATE, 'YYYYMMDD') as STR_DATE";
	public static final String SQL_DATE_MYSQL="select date_format(now(),'%Y%m%d') STR_DATE";
	public static final String SQL_DATE_SQLSERVER="select CONVERT(varchar(12) , getdate(), 112 ) STR_DATE";
	public static final String SQL_DATE_DB2="SELECT int(date(current date)) as STR_DATE FROM sysibm.sysdummy1";
	public static final String SQL_DATE_SYBASE="select CONVERT(varchar(12) , getdate(), 112 ) STR_DATE";
	public static final String SQL_DATE_SQLITE="select strftime('%Y/%m/%d','now')";
	
	
	
}
