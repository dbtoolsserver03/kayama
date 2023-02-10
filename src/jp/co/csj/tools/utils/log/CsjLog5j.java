/**
 *
 */
package jp.co.csj.tools.utils.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import jp.co.csj.tools.utils.constant.CsjConst;
import jp.co.csj.tools.utils.str.CsjStrUtils;

/**
 * @author Think
 *
 */
public class CsjLog5j {
	public static BufferedWriter s_log5j = null;
	public  static void initLog5j(String logPath,String fileNm, String enCode) throws Throwable {
		File file = new File(logPath);
		file.mkdirs();
		s_log5j = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(logPath + fileNm), enCode));
	}

	public static void closeLog5j() throws Throwable {
		// TODO Auto-generated method stub
		if (s_log5j != null) {
			s_log5j.close();
			s_log5j=null;
		}
	}

	public static void write(String str) {
		write(str);
	}


	public static String addlrBracketsM(String str,boolean isBlankNotPrint) {

		if (CsjStrUtils.isEmpty(str)) {
			if (isBlankNotPrint) {
				return "";
			}
		}
		return "[" + str + "]";
	}
	public static String addlrBracket_M_L_JP(String str, boolean isBlankNotPrint) {
		if (CsjStrUtils.isEmpty(str)) {
			if (isBlankNotPrint) {
				return "";
			}
		}
		return CsjConst.Z_SIGN_BRACKETS_M_L_JP + str + CsjConst.Z_SIGN_BRACKETS_M_R_JP;
	}

	/**
	 * @param writer
	 * @param line
	 */
	public static void writeLine(BufferedWriter writer, String line) throws Throwable {
			writer.write(line);
			writer.newLine();
			writer.flush();
		
	}

	/**
	 * @param writer
	 * @throws IOException 
	 */
	public static void close(BufferedWriter writer) throws Throwable {
			if (writer!=null) {
				writer.close();
				writer=null;
			}

	}
}
