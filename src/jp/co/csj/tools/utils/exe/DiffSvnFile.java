package jp.co.csj.tools.utils.exe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import jp.co.csj.tools.utils.constant.CsjConst;
import jp.co.csj.tools.utils.constant.CsjProcess;
import jp.co.csj.tools.utils.date.CsjDateUtil;
import jp.co.csj.tools.utils.file.CsjDirectory;
import jp.co.csj.tools.utils.file.CsjFileUtils;
import jp.co.csj.tools.utils.poi.CsjPoiUtils;
import jp.co.csj.tools.utils.str.CsjStrUtils;


public class DiffSvnFile {

	private static String VERSION="-- ver 20210429 by saisk --";
	private static Logger log = Logger.getLogger(DiffSvnFile.class);
	private static Set<String> setEndStr;
	private static Set<String> setFolderStr;
	private static String encode;
	private static String autoFolder;

	public static Set<String> newFileSet = new TreeSet<String>();
	public static void main(String[] args) {

		log.info(VERSION);
		Date beginTime = new Date();
		try {
			run(args);
			log.info("diff success");
		} catch (Throwable e) {
			for (String s : CsjStrUtils.getThrowableMsg(e)) {
				log.error(s);
			}
			e.printStackTrace();
		}
		Date endTime = new Date();
		try {
			log.info("beginTime:["+CsjDateUtil.getFormatDateTime(beginTime, CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24_SSS) + "] endTime:["+CsjDateUtil.getFormatDateTime(endTime,  CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24_SSS) + "]costTime:["+CsjDateUtil.getMsHour(endTime.getTime()-beginTime.getTime())+"]");
		} catch (Throwable e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private static void run(String[] args) throws Throwable {

		Properties prop = new Properties();
		String propFile = "";
		if (args == null || args.length == 0) {
			propFile="DiffSvnFile.properties";
		} else {
			propFile=args[0];
		}
		prop.load(new BufferedReader(new FileReader("prop/"+args[0])));
		
		setEndStr = CsjStrUtils.getSetFromArray(String.valueOf(prop.get("ignore.file.end")).split(","));
		setFolderStr = CsjStrUtils.getSetFromArray(String.valueOf(prop.get("ignore.file.folder")).split(","));
		encode = String.valueOf(prop.get("source.encode"));

		log.info("properties file :" + propFile);
		log.info("source.encode=" + encode);
		log.info("ignore.file.end=" + setEndStr);
		log.info("ignore.file.folder=" + setFolderStr);
		
		String svnFolder = String.valueOf(prop.get("source.svn.folder"));
		String locFolder = String.valueOf(prop.get("source.local.folder"));
		String[] pjName = String.valueOf(prop.get("source.pj")).split(",");
		
		log.info("source.local.folder=" + locFolder);
		log.info("source.svn.folder=" + svnFolder);
		log.info("source.pj=" + setFolderStr);
		
		autoFolder=CsjDateUtil.getFormatDateTime(new Date(), CsjConst.YYYYMMDDHH_MMSSMINUS_24);
		
		List<DiffBean> diffBeanLst = new ArrayList<DiffBean>();
		
		for (int i = 0; i < pjName.length; i++) {
			String locPath = locFolder+ pjName[i];
			String svnPath = CsjProcess.s_pj_path+svnFolder+pjName[i];
			log.debug("compare path:["+locPath+"] svn["+svnPath+"]");
			Map<String, File> fMapSvn = getFilesList(svnPath, true, pjName[i]);
			Map<String, File> fMapLocal = getFilesList(locPath, true, pjName[i]);

			for (Entry<String,File> entrySvn:fMapSvn.entrySet()) {
				File fSvn = entrySvn.getValue();
				File fLoc = fMapLocal.get(entrySvn.getKey());
				if (fLoc == null) {
					diffBeanLst.add(new DiffBean("削除",fSvn.getName(),entrySvn.getKey().replace("\\","/"),null,"",CsjFileUtils.getFileTime(fSvn,CsjConst.YYYY_MM_DD_SLASH)));
					log.info("delete:"+entrySvn.getKey().replace("\\", "/")+"\t" + CsjFileUtils.getFileTime(fSvn, CsjConst.YYYY_MM_DD_SLASH));
				}else {
					log.debug("compare:"+fLoc.getAbsolutePath());
					if (CsjFileUtils.isSameFile(fSvn,fLoc,encode)) {
						
					} else {
						newFileSet.add(fLoc.getName());
						diffBeanLst.add(new DiffBean("修正",fLoc.getName(),entrySvn.getKey().replace("\\","/"),new Date(fLoc.lastModified()),CsjFileUtils.getFileTime(fLoc,CsjConst.YYYY_MM_DD_SLASH),CsjFileUtils.getFileTime(fSvn,CsjConst.YYYY_MM_DD_SLASH)));

						CsjDirectory.copyFile(fLoc.getAbsolutePath(), autoFolder+ CsjProcess.s_f_s+"loc"+CsjProcess.s_f_s+entrySvn.getKey().substring(0,entrySvn.getKey().lastIndexOf(CsjProcess.s_f_s)));
						CsjDirectory.copyFile(fSvn.getAbsolutePath(), autoFolder+ CsjProcess.s_f_s+"svn"+CsjProcess.s_f_s+entrySvn.getKey().substring(0,entrySvn.getKey().lastIndexOf(CsjProcess.s_f_s)));

					
					}
				}
			}
			
		}
		
		wrtiteXls(diffBeanLst);
	}

	private static void wrtiteXls(List<DiffBean> diffBeanLst) throws Throwable {
		// TODO 自動生成されたメソッド・スタブ
		
		Workbook wb = CsjPoiUtils.getWorkBook("tmp"+CsjProcess.s_f_s + "DiffSvnFile.xls");
		
		Sheet st = wb.getSheet("tmp");
		for (int i = 0; i < diffBeanLst.size(); i++) {

			DiffBean diffBean = diffBeanLst.get(i);
			CsjPoiUtils.setCellValue(st,i+2,0,String.valueOf(i+1));
			CsjPoiUtils.setCellValue(st,i+2,1,diffBean.fileNm);
			CsjPoiUtils.setCellValue(st,i+2,2,diffBean.locFilePath);
			CsjPoiUtils.setCellValue(st,i+2,5,diffBean.type);
			
			if (diffBean.locDate != null) {
				CsjPoiUtils.setCellValue(st,i+2,7,CsjDateUtil.getFormatDateTime(diffBean.locDate, CsjConst.YYYY_MM_DD_SLASH));
				CsjPoiUtils.setCellValue(st,i+2,8,CsjDateUtil.getFormatDateTime(diffBean.locDate, CsjConst.YYYY_MM_DD_SLASH));
			}
		}
		CsjPoiUtils.writeXls(wb, autoFolder, autoFolder+".xls");
	}

	private static Map<String, File> getFilesList(String folder, boolean b, String pjName) {

		List<File> fileList = CsjDirectory.getFilesList(folder, b);
		Map<String,File> fileMap = new TreeMap<String,File>();
		for (File file : fileList) {
			if (CsjStrUtils.isStrEndLikeSet(file.getAbsolutePath(),setEndStr)
					||CsjStrUtils.isStrEndLikeSet(file.getAbsolutePath(),setEndStr)) {
				continue;
			}
			fileMap.put(pjName+CsjStrUtils.fromAtoB(file.getAbsolutePath(), pjName, ""),file);
		}
		return fileMap;
	}
	

}
class DiffBean {

	public String type;
	public String fileNm;
	public String locFilePath;
	public Date locDate;
	public String locTime;
	public String svnTime;
	public DiffBean(String type, String fileNm, String locFilePath, Date locDate, String locTime, String svnTime) {
		super();
		this.type = type;
		this.fileNm = fileNm;
		this.locFilePath = locFilePath;
		this.locDate = locDate;
		this.locTime = locTime;
		this.svnTime = svnTime;
	}
	
}
