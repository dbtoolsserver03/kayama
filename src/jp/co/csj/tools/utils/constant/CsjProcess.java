/**
 *
 */
package jp.co.csj.tools.utils.constant;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.Properties;

import jp.co.csj.tools.utils.date.CsjDateUtil;

/**
 * @author Think
 *
 */
public class CsjProcess {

	public static String s_pj_path = CsjConst.EMPTY;
	public static int s_pj_path_length = 0;
	public static String s_f_s = CsjConst.EMPTY;
	public static String s_user = CsjConst.EMPTY;
	public static String s_local_host = CsjConst.EMPTY;
	public static String s_local_userdomain = CsjConst.EMPTY;
	public static String s_local_usercountry = CsjConst.EMPTY;
	
	public static String s_local_os = CsjConst.EMPTY;
	public static String s_local_os_arch = CsjConst.EMPTY;
	
	
	public static String s_out_ip = CsjConst.EMPTY;
	public static String s_local_inet_addr_with_line = CsjConst.EMPTY;
	public static String s_local_inet_addr = CsjConst.EMPTY;
	public static Date s_default_date;
	
	public static final String s_log4j_file_path = "";
	public static String s_pc_info = CsjConst.EMPTY;
	public static String s_db_log_pc_info = CsjConst.EMPTY;
	public static String   s_newLine   =   CsjConst.EMPTY;

	static {

		try {
			CsjDateUtil.getDate("1901/01/01", CsjConst.YYYY_MM_DD_SLASH);
			Properties   pp   =   System.getProperties();
			s_newLine = pp.getProperty( "line.separator");
			s_f_s = System.getProperty("file.separator");

			// 取得当前路径
			s_pj_path = System.getProperty("user.dir") + s_f_s;
			s_pj_path_length = s_pj_path.length();
			
			s_local_usercountry = System.getProperty("user.country");

			s_local_os = System.getProperty("os.name");
			s_local_os_arch = System.getProperty("os.arch");
			s_local_host =InetAddress.getLocalHost().toString();
			s_user = System.getenv().get("USERNAME");// 获取用户名
			

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void openFile(String filePath)  {
		try {
			Runtime.getRuntime().exec("cmd /c start "+ filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void delFile(String filePath) {

		Process process = null;
		File f = new File(filePath);
		try {

			if (f.isDirectory()) {
				process = Runtime.getRuntime().exec("cmd /c  rd /q /s " + f.getAbsolutePath());
				process.waitFor();
			} else if (f.isFile()) {
				process = Runtime.getRuntime().exec("cmd /c del " + f.getAbsolutePath());
				process.waitFor();
			} else {
				System.out.println("error");
				return;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public static void copyFile(String fromFilePath, String toFilePath) {
		try {
			Process process = null;
			File fromFile = new File(fromFilePath);
			File toFile = new File(toFilePath);
			toFile.mkdirs();
			String batStr = "xcopy " + fromFile.getAbsolutePath() + "\\*.* " + toFile.getAbsolutePath()
					+ "\\ /y /e /k /c /R";
			process = Runtime.getRuntime().exec(batStr);
			process.waitFor();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
