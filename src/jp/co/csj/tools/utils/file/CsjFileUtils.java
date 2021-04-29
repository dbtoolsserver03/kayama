package jp.co.csj.tools.utils.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import jp.co.csj.tools.utils.constant.CsjConst;
import jp.co.csj.tools.utils.date.CsjDateUtil;
import jp.co.csj.tools.utils.log.CsjLog4j;
import jp.co.csj.tools.utils.log.CsjLog5j;

public class CsjFileUtils {
	public static void writeWithbBlank(BufferedWriter writer, String str,
			int blankCount) {

		if (writer == null) {
			return;
		}
		try {
			writer.write(getBlank(str, blankCount, true));
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			CsjLog4j.logger.info(e.getMessage());
		}

	}

	public static int checkFilePath(String text) throws Throwable{
		int retVal = CsjFileConst.IS_ERROR;

		File f = new File(text);
		if (f.isDirectory()) {
			retVal = CsjFileConst.IS_PATH;
		} else if (f.isFile()) {
			retVal = CsjFileConst.IS_FILE;
		} else if (text.contains(";") && CsjDirectory.isFiles(text, ";")) {
			retVal = CsjFileConst.IS_FILES;
		}
		return retVal;
	}

	public static String getTxtFileInfo(String fileNm, String encode,
			String splitLine) throws Throwable {
		StringBuffer buffer = new StringBuffer();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileNm), encode));

		while (reader.ready()) {
			buffer.append(reader.readLine() + splitLine);
		}

		return buffer.toString();

	}

	public static String getBlank(String str, int blankCount, boolean isBefore) {
		String strBlank = CsjConst.EMPTY;
		for (int i = 0; i < blankCount; i++) {
			strBlank += "    ";
		}
		if (isBefore) {
			return strBlank + str;
		} else {
			return str + strBlank;
		}

	}

	/**
	 * @param writer
	 * @param copyRightList
	 * @param blankCount
	 */
	public static void writeWithbBlank(BufferedWriter writer,
			List<String> strList, int blankCount) {
		for (String str : strList) {
			writeWithbBlank(writer, str, blankCount);
		}
	}

	/**
	 * @param writer
	 * @param copyRightList
	 * @param blankCount
	 */
	public static void writeWithbBlank(BufferedWriter writer, String paraStr,
			List<String> strList, int blankCount,
			boolean isLeft) {
		for (String str : strList) {
			if (isLeft) {
				writeWithbBlank(writer, paraStr + str, blankCount);
			} else {
				writeWithbBlank(writer, str + paraStr, blankCount);
			}

		}
	}

	/**
	 * @param writer
	 * @param importMap
	 * @param blankCount
	 */
	public static void writeWithbBlank(BufferedWriter writer, String paraStr,
			Map<String, String> map, int blankCount,
			boolean isLeft) {
		for (Entry<String, String> entry : map.entrySet()) {

			if (isLeft) {
				writeWithbBlank(writer, paraStr + entry.getKey(), blankCount);
			} else {
				writeWithbBlank(writer, entry.getKey() + paraStr, blankCount);
			}
		}
	}

	/**
	 * @param writer
	 * @param importMap
	 * @param blankCount
	 */
	public static void writeWithbBlank(BufferedWriter writer,
			Map<String, String> map, int blankCount) {
		for (Entry<String, String> entry : map.entrySet()) {
			writeWithbBlank(writer, entry.getKey(), blankCount);
		}
	}

	public static LinkedHashMap<String, String> getFileMap(String filePath,
			String encode, boolean isKeyLineNo) {

		LinkedHashMap<String, String> retMap = new LinkedHashMap<String, String>();

		long retVal = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(filePath)),
					encode));

			while (reader.ready()) {
				if (isKeyLineNo) {
					retMap.put(String.valueOf(retVal++), reader.readLine());
				} else {
					retMap.put(reader.readLine(), String.valueOf(retVal++));
				}
			}
			reader.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return retMap;
	}


	public static void writeFile(String logPath, String fileNm, String enCode,
			List<String> strList) throws Throwable {
		File file = new File(logPath);
		file.mkdirs();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(logPath + fileNm), enCode));
		for (String str : strList) {
			writeWithbBlank(writer, str, 0);
		}
		writer.close();
	}
	public static boolean isFileExist(String filePath) {
		return new File(filePath).isFile();
	}
	public static boolean isFolderExist(String folderPath) {
		return new File(folderPath).isDirectory();
	}
	public static void reWriteFile(String filePath, String enCode,
			Map<String,String> regexMap,String cr) throws Throwable {
		List<String> strLst = getFileContent(filePath, enCode);
		List<String> strLstTmp = new ArrayList<String>();
		for (String s:strLst) {
			for (String key : regexMap.keySet()) {
				if (s.matches(key)) {
					s = regexMap.get(key);
					break;
				}
			}
			strLstTmp.add(s);
		}

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(filePath), enCode));
		for (String str : strLstTmp) {
			writer.write(str);
			writer.write(cr);
		}
		writer.close();
	}
	
	public static String getFileNm(String filePath) {
		String retVal = CsjConst.EMPTY;
		File file = new File(filePath);
		retVal = file.getName();
		return retVal;
	}

	public static boolean
			isTwoFileNmSame(String fileOldPath, String filNewPath) {
		return getFileNm(fileOldPath).equals(getFileNm(filNewPath));
	}

	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> getFileContent(File f, String encode)
			throws Throwable {
		List<String> retList = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(f),
				encode));

		while (reader.ready()) {
			retList.add(reader.readLine());
		}
		reader.close();
		return retList;
	}
	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> getFileContent(String path, String encode)
			throws Throwable {
		List<String> retList = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(path)),
				encode));

		while (reader.ready()) {
			retList.add(reader.readLine());
		}
		reader.close();
		return retList;
	}
	public static Map<Integer,String> getFileContents(String filePath,String splitCh,int col,int rowStart,int rowEnd,String encode) throws Throwable {
		Map<Integer,String> retMap = new HashMap<Integer, String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filePath)),
				encode));

		int i = 0;
		while (reader.ready()) {
			String line = reader.readLine();
			if (i >= rowStart && i <= rowEnd) {
				String[] sa = line.split(splitCh);
				if (col < sa.length ) {
					retMap.put(i-rowStart, sa[col]);
				}
			}
			i++;
		}
		reader.close();

		return 	retMap;
	}
	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> getFilesContent(String path, String encode)
			throws Throwable {

		List<File> fileList = CsjDirectory.getFilesList(path, false);
		List<String> retList = new ArrayList<String>();
		for (File f : fileList) {
			List<String> strList = getFileContent(f, encode);
			for (String str : strList) {
				retList.add(str);
			}
		}

		return retList;
	}

	/**
	 * @param oldFilesMap
	 * @param newFilesMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void copyFile(String path, String nm, String newPath,
			String newNm, String encode) throws Throwable {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(path + nm),
				encode));
		File file = new File(newPath);
		file.mkdirs();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(newPath + newNm), encode));

		while (reader.ready()) {
			writeWithbBlank(writer, reader.readLine(), 0);
		}

		writer.close();
		reader.close();
	}

	public static void main(String[] args) {
		try {
			CsjLog5j.initLog5j("d:\\", "a.txt", CsjFileConst.ENCODE_UTF_8);
			List<String> retList = getFilesContent(
					"D:\\javaTools\\eclipse\\workspace\\CsjToolsPic\\AutoDb\\xls_to_createTblSql_at_20120420_174311",
					CsjFileConst.ENCODE_UTF_8);
			CsjFileUtils.writeWithbBlank(CsjLog5j.s_log5j, retList, 0);
			CsjLog5j.closeLog5j();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param fileMap
	 * @param string
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void createFile(TreeMap<Long, File> fileMap, String outputFileName, String encode) throws Throwable {
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFileName), encode));
			for (File f : fileMap.values()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(f.getAbsolutePath()),encode));
				while (reader.ready()) {
					String line = reader.readLine();
					CsjLog5j.writeLine(writer, line);
				}
				reader.close();
			}
			CsjLog5j.close(writer);
		} catch (Throwable e) {
			throw e;
		}
	}

	public static String getFileTime(File f, String yyyyMmDdSlash) throws Throwable {
		return CsjDateUtil.getDateTime(f.lastModified(),yyyyMmDdSlash);
	}

	public static boolean isSameFile(File f1, File f2, String encode) throws Throwable {
		List<String> lst1 = getFileContent(f1, encode);
		List<String> lst2 = getFileContent(f2, encode);

		if (lst1.size()!=lst2.size()) {
			return false;
		}
		for (int i=0; i < lst1.size(); i++) {
			if (!lst1.get(i).equals(lst2.get(i))) {
				return false;
			}
		}
		return true;
	}
}
