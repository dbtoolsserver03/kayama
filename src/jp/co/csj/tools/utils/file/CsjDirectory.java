package jp.co.csj.tools.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.co.csj.tools.utils.constant.CsjProcess;
import jp.co.csj.tools.utils.reg.RegConstStr;
import jp.co.csj.tools.utils.str.CsjStrUtils;
public class CsjDirectory {

	public static boolean isFiles(String text, String splitStr) {

		boolean retVal = true;
		String[] strArr = text.split(splitStr);
		for (String str : strArr) {
			if (CsjStrUtils.isEmpty(str)) {
				continue;
			}
			File f = new File(str);
			if (f.isFile() == false) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}

	public static int checkFilePath(String filePath) {
		int retVal = CsjFileConst.IS_ERROR;
		File f = new File(filePath);
		if (f.isDirectory()) {
			retVal = CsjFileConst.IS_PATH;
		} else if (f.isFile()) {
			retVal = CsjFileConst.IS_FILE;
		} else if (filePath.contains(";") && isFiles(filePath, ";")) {
			retVal = CsjFileConst.IS_FILES;
		}
		return retVal;
	}
	public static LinkedList<File> getExcelFileByAbsPath(String filePath,boolean isHaveSubFile) {
		LinkedList<File> retList = new LinkedList<File>();
		int retVal = checkFilePath(filePath);
		if (retVal == CsjFileConst.IS_PATH) {
			retList= getFilesListReg(filePath, isHaveSubFile,RegConstStr.EXCEL_REG_DOT);
		} else if (retVal == CsjFileConst.IS_FILE) {
			retList.add(new File(filePath));
		} else if (retVal == CsjFileConst.IS_FILES) {
			String[] strArr = filePath.split(",");
			for (String str : strArr) {
				if (CsjStrUtils.isNotEmpty(str)) {
					retList.add(new File(str));
				}
			}
		}
		return retList;
	}
	public static LinkedList<File> getFilesListWithStr(String filePath,
			boolean isHaveSubFile,String startStr,String endStr) {

		LinkedList<File> tList =getFilesList(filePath,isHaveSubFile);
		LinkedList<File> retList = new LinkedList<File>();
		for (File f : tList) {
			String fNm = f.getName().toLowerCase();
			if (CsjStrUtils.isNotEmpty(startStr)&& fNm.startsWith(startStr)) {
					retList.add(f);
			} else if (CsjStrUtils.isNotEmpty(endStr)&& fNm.endsWith(endStr)) {
				retList.add(f);
			}
		}
		return retList;
	}
	public static TreeMap<Long,File> getFilesModifyTimeMapReg(String filePath,
			boolean isHaveSubFile,String regex) {

		LinkedList<File> tList =getFilesList(filePath,isHaveSubFile);
		TreeMap<Long,File> retMap = new TreeMap<Long, File>();
		for (File f : tList) {
			String fNm = f.getName().toLowerCase();
			
			if (CsjStrUtils.isEmpty(regex)||fNm.matches(regex)) {
				retMap.put(f.lastModified(), f);
			}
		}
		return retMap;
	}
	public static LinkedList<File> getFilesListReg(String filePath,
			boolean isHaveSubFile,String regex) {

		LinkedList<File> fList = getFilesList(filePath,isHaveSubFile);
		LinkedList<File> retList = new LinkedList<File>();

		for (File f : fList) {
			if (CsjStrUtils.isEmpty(regex)||f.getName().matches(regex)) {
				retList.add(f);
			}
		}
		return retList;
	}

	public static LinkedList<File> getFilesList(String filePath,
			boolean isHaveSubFile) {

		LinkedList<File> retList = new LinkedList<File>();
		LinkedList<File> list = new LinkedList<File>();
		File dir = new File(filePath);
		File file[] = dir.listFiles();
		if (null == file) {
		return retList;
		}

		for (int i = 0; i < file.length; i++) {

			if (file[i].isDirectory())
				list.add(file[i]);
			else
				retList.add(file[i]);
		}

		if (isHaveSubFile) {
			File tmp;
			while (!list.isEmpty()) {
				tmp = list.removeFirst();

				if (tmp.isDirectory()) {
					file = tmp.listFiles();
					if (file == null)
						continue;
					for (int i = 0; i < file.length; i++) {
						if (file[i].isDirectory())
							list.add(file[i]);
						else {
							retList.add(file[i]);
						}
					}
				} else {
					retList.add(tmp);
				}
			}
		}

		return retList;
	}

	public static boolean reNameFile(File f, String newNm) {
		File mm = new File(f.getParent() + CsjProcess.s_f_s + newNm);
		return f.renameTo(mm);
	}
	
	
	public static Map<String, File> getFilesMap(String filePath, boolean isHaveSubFile) {
		LinkedHashMap<String, File> retMap = new LinkedHashMap<String, File>();
		List<File> fileList = getFilesList(filePath, isHaveSubFile);
		for (File f : fileList) {
			retMap.put(f.getName(), f);
		}
		return retMap;
	}
	public static Map<String, File> getFilesTreeMap(String filePath, boolean isHaveSubFile) {
		Map<String, File> retMap = new TreeMap<String, File>();
		List<File> fileList = getFilesList(filePath, isHaveSubFile);
		for (File f : fileList) {
			retMap.put(f.getName(), f);
		}
		return retMap;
	}

	public static String formatFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	public static long getFileMaxLine(File f) throws Throwable {

		long retVal = 0;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), CsjFileConst.ENCODE_SHIFT_JIS));

			while (reader.ready()) {
				reader.readLine();
				retVal++;
			}
			reader.close();
		return retVal;
	}
	public static void removeFiles(String filePath) throws Throwable {
			File f = new File(filePath);
			if (f!=null) {
				delFile(f);
			}
	}

	public static void delFile(String path,String fileNmReg,boolean haveSub) throws Throwable {
			File f = new File(path);
			if (CsjStrUtils.isEmpty(fileNmReg)) {
				delFile(f);
			}else if (f.isDirectory()) {
				List<File> fLst = getFilesList(f.getAbsolutePath(), haveSub);
				for (File ft : fLst) {
					if (CsjStrUtils.isEmpty(fileNmReg)) {
						ft.delete();
					}else if (ft.getName().matches(fileNmReg)) {
						ft.delete();
					}
				}
			}

	}
	public static void delFile(File f)throws Throwable {
			if (f == null) {
				return;
			}
			if (f.isDirectory()) {
				File[] list = f.listFiles();
				for (int i = 0; i < list.length; i++) {
					if (list[i].isDirectory()) {
						delFile(list[i]);
					}else{
						if(list[i].isFile())
							list[i].delete();
					}
				}
				f.delete();
			} else {
				if (f.isFile())
					f.delete();
			}
	}


	public static String formatToFolder(String folder) {
		if (folder.endsWith(CsjProcess.s_f_s)) {
			return folder;
		} else {
			return folder + CsjProcess.s_f_s;
		}
	}

	public static void copyFile(String fromFilePath, String toPath) throws Throwable {

		File fromFile = new File(fromFilePath);
		File toFile=new File(toPath);
		toFile.mkdirs();
		String command = "cmd /c copy "+ fromFile.getAbsolutePath() + " " + toFile.getAbsolutePath() + CsjProcess.s_f_s;
		Runtime.getRuntime().exec(command).waitFor();
	}


}
