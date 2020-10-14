/**
 * Copyright (c) 2012, Java CNC/CSJ for tools. All rights reserved.
 */
package jp.co.csj.tools.utils.str;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.csj.tools.utils.constant.CsjConst;
import jp.co.csj.tools.utils.constant.CsjProcess;
import jp.co.csj.tools.utils.date.CsjDateUtil;
import jp.co.csj.tools.utils.file.CsjFileConst;
import jp.co.csj.tools.utils.log.CsjLog4j;
import jp.co.csj.tools.utils.poi.CsjPosition;
import jp.co.csj.tools.utils.reg.RegConstStr;

/**
 * CsjStrUtils
 *
 * @author cuishuangjia@163.com
 * @version 1.0
 * @since 1.6
 */
public class CsjStrUtils {

	/**
	 * @param text
	 * @return
	 */
	public static String convertTxtToReg(String text) {
		
		String retStr = text.replace("\\t", "\t");
		retStr = retStr.replace("\\0", "\0");
		retStr = retStr.replace("\\n", "\n");
		retStr = retStr.replace("\\r", "\r");
		return retStr;
	}
	
	public static boolean isMatchIgnorReg(String str, String reg) {
		if (isNotEmpty(str)) {
			return str.toLowerCase().matches(reg.toLowerCase());
		}
		return false;
	}
	public static boolean isEndByIgnor(String str, String end) {
		if (isNotEmpty(str)) {
			return str.toLowerCase().endsWith(end.toLowerCase());
		}
		return false;
	}
	public static int getBitesLength(String str) {
		int ret = 0;

		if (isNotEmpty(str)) {
			try {
				ret = str.getBytes(CsjFileConst.ENCODE_SHIFT_JIS).length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static String getNoNullStr(String str) {
		String ret = str;
		
		if (isEmpty(str)) {
			ret = CsjConst.EMPTY;
		}
		return ret;
	}
	// 判断字符串是否为空(为NULL或者为"")
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return  obj.toString().length() == 0;
		} else if (obj instanceof List) {
			return ((List)obj).size()==0;
		} else if (obj instanceof Map) {
			return ((Map)obj).size()==0;
		} else if (obj instanceof Set) {
			return ((Set)obj).size()==0;
		} else if (obj instanceof Object[]) {
			return ((Object[])obj).length==0;
		}
		return false;
	}
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static boolean isEqual(Object oldObj,Object newObj) {
		boolean retVal = false;
		if (oldObj==null ||newObj==null) {
			return retVal;
		}
		if (oldObj instanceof String && newObj instanceof String) {
			if (String.valueOf(oldObj).equals(newObj)) {
				retVal = true;
			}
		}
		return retVal;
	}

	public static boolean isNotEqual(Object oldObj,Object newObj) {
		return !isEqual(oldObj,newObj);
	}
	public static boolean isEqualIgnorBigSmall(String oldstr, String newstr,boolean trimed) {
		boolean retVal = false;
		if (oldstr == null || newstr == null) {
			return retVal;
		}
		if (trimed) {
			oldstr = fromAtoBByTrim(oldstr, "", "");
			newstr = fromAtoBByTrim(newstr, "", "");
		}
		if (CsjStrUtils.toLowOrUpStr(oldstr).equals(CsjStrUtils.toLowOrUpStr(newstr))) {
			retVal = true;
		}
		return retVal;
	}

	public static boolean isUptOrIns(String sql) {
		if (CsjStrUtils.isEmpty(sql)) {
			return false;
		}
		sql = lrTrimSpace(sql);
		if (sql.toLowerCase().startsWith("update")||sql.toLowerCase().startsWith("insert")||sql.toLowerCase().startsWith("delete")) {
			return true;
		}
		return false;
	}
	/**
	 * @param paraStr
	 * @param string
	 * @param isWithBlank
	 */
	public static List<String> getListStrSplit(String paraStr, String splitCh,
			boolean hasEmpty,boolean islrtrim) {

		String[] arrStr = paraStr.split(splitCh,-1);
		List<String> retStrList = new ArrayList<String>();

		for (String str : arrStr) {
			if (islrtrim) {
				str = CsjStrUtils.lrTrimSpace(str);
			}
			if (hasEmpty == false && CsjStrUtils.isEmpty(str)) {
				continue;
			}

			retStrList.add(str);
		}
		return retStrList;
	}

	
	public static List<CsjPosition> getPointList(String str,Set<String> set) {

		List<CsjPosition> retList = new ArrayList<CsjPosition>();
		str = str.toLowerCase();
		
		for (String s : set) {
			int preLenth = 0;
			String strTemp = str;
			while (strTemp.contains(s)) {
				int startPos = strTemp.indexOf(s);
				int endPos = startPos+s.length();
				retList.add(new CsjPosition(preLenth+startPos, s.length()));
				strTemp = strTemp.substring(endPos);	
				preLenth+=endPos;
			}
		}

		return retList;
	}

	
	/**
	 * @param str
	 * @param strA
	 * @param strB
	 * @return String
	 */
	public static String fromAtoBByTrim(String str, String strA, String strB) {
		String ret = CsjConst.EMPTY;

		if (CsjStrUtils.isNotEmpty(str)) {
			ret = fromAtoB(str, strA, strB);
			ret = lrTrimSpace(ret);
		}

		return ret;

	}
	public static String hashSet2StrBy(HashSet<String> set, String ch) {
		StringBuffer sb = new StringBuffer();
		if (CsjStrUtils.isNotEmpty(set)) {
			Object[] objArr = set.toArray();

			for (int i = 0; i < objArr.length; i++) {
				if (i + 1 == objArr.length) {
					sb.append(String.valueOf(objArr[i]));
				} else {
					sb.append(String.valueOf(objArr[i]));
					sb.append(ch);
				}
			}
		}
		return sb.toString();

	}
	public static String escapeNull(String str) {
		if (CsjStrUtils.isEmpty(str)) {
			return "";
		} else {
			return str;
		}
	}
	/**
	 * @param ret
	 * @return String
	 */

	public static String lrTrimSpace(String str) {

		if (isEmpty(str)) {
			return CsjConst.EMPTY;
		}

		str = str.trim();
		while (str.startsWith(" ")
				|| str.startsWith(CsjConst.Z_SIGN_SPACE_1)) {
			str = str.substring(1, str.length());
		}
		while (str.endsWith(" ")
				|| str.endsWith(CsjConst.Z_SIGN_SPACE_1)) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public static String lrTrimOnlyOne(String str, String ch,boolean isHead) {

		if (CsjStrUtils.isEmpty(str)) {
			return CsjConst.EMPTY;
		} else if (CsjStrUtils.isEmpty(ch)) {
			return str;
		}

		if (isHead) {
			if (str.startsWith(ch)) {
				str = str.substring(1, str.length());
			}
		} else {
			if (str.endsWith(ch)) {
				str = str.substring(0, str.length() - 1);
			}
		}

		return str;
	}

	public static String lrTrimStartEndBySet(String str, Set<String> chSet,boolean isStart) {

		if (CsjStrUtils.isEmpty(str)) {
			return CsjConst.EMPTY;
		} else if (CsjStrUtils.isEmpty(chSet)) {
			return str;
		}

		if (isStart) {
			while(true){
				if (CsjStrUtils.isEmpty(str)) {
					return str;
				}
				String begin = String.valueOf(str.charAt(0));
				if (chSet.contains(begin)) {
					str = str.substring(1, str.length());
				} else {
					break;
				}
			}

		} else {
			while(true){
				if (CsjStrUtils.isEmpty(str)) {
					return str;
				}
				String begin = String.valueOf(str.charAt(str.length() - 1));
				if (chSet.contains(begin)) {
					str = str.substring(0, str.length() - 1);
				} else {
					break;
				}
			}
		}

		return str;
	}
	/**
	 * @param str
	 * @param strA
	 * @param strB
	 * @return String
	 */
	public static String fromAtoB(String str, String strA, String strB) {

		if (null == strA) {
			strA = CsjConst.EMPTY;
		}
		if (null == strB) {
			strB = CsjConst.EMPTY;
		}
		String temp = CsjConst.EMPTY;
		int n = 0;
		if (CsjConst.EMPTY.equals(strA)) {
			if (CsjConst.EMPTY.equals(strB)) {
				return str;
			}
			if (str.contains(strB)) {
				return str.substring(0, str.indexOf(strB));
			}
			return str;
		}

		if (CsjConst.EMPTY.equals(strB)) {
			if (!CsjConst.EMPTY.equals(strA)) {
				if (str.contains(strA)) {
					n = str.indexOf(strA) + strA.length();
					if (n < str.length()) {
						return str.substring(n);
					} else if (n == str.length()) {
						return CsjConst.EMPTY;
					}
					return str;
				}
				return str;
			}
		}

		if (str.contains(strA)) {
			n = str.indexOf(strA) + strA.length();
			if (n < str.length()) {
				temp = str.substring(n);
			}

			if (temp.contains(strB)) {
				return temp.substring(0, temp.indexOf(strB));
			}
			return temp;
		} else {
			if (str.contains(strB)) {
				return str.substring(0, str.indexOf(strB));
			}
			return str;
		}
	}

	/**
	 *
	 * @param str
	 * @param listSignMethod
	 * @param strB
	 * @return
	 */
	public static String fromAtoBByTrimWithStartList(String str,
			List<String> listSignMethod, String strB) {
		// TODO Auto-generated method stub
		for (String string : listSignMethod) {
			if (str.contains(string)) {
				str = fromAtoBByTrim(str, string, strB);
			}
		}
		return str;
	}

	/**
	 *
	 * @param str
	 * @param listSignMethod
	 * @param strB
	 * @return
	 */
	public static String fromAtoBByTrimWithEndList(String str,
			String strA, List<String> list) {
		for (String string : list) {
			if (str.contains(string)) {
				str = fromAtoBByTrim(str, strA, string);
			}
		}
		return str;
	}

	public static String funReplace(String rStr, String rFix, String rRep) {
		if (isEmpty(rStr)) {
			return "";
		}
		int l = 0;
		String gRtnStr = rStr;
		do {
			l = rStr.indexOf(rFix, l);
			if (l == -1)
				break;
			gRtnStr = rStr.substring(0, l) + rRep
					+ rStr.substring(l + rFix.length());
			l += rRep.length();
			rStr = gRtnStr;
		} while (true);
		return gRtnStr.substring(0, gRtnStr.length());
	}

	// 首字母转小写
	public static String toLowerCaseFirstOne(String s) {
		if (CsjStrUtils.isEmpty(s))
			return "";
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toLowerCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

	public static String toStr(Object obj) {
		if (isNotEmpty(obj)) {
			return String.valueOf(obj);
		}

		return "";
	}
	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		if (CsjStrUtils.isEmpty(s)) {
			return CsjConst.EMPTY;
		}
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

//	public static List<HashMap<String, String>> toUpperKeyList(List<HashMap<String, String>> list, HashSet<String> setCol) {
//
//		List<HashMap<String, String>> retList = new ArrayList<HashMap<String,String>>();
//		for(HashMap<String, String> map : list) {
//			HashMap<String, String> inMap = new HashMap<String, String>();
//			for (Entry<String, String> entry : map.entrySet()) {
//				String key = toLowOrUpStr(entry.getKey());
//				String val = entry.getValue();
//				if (setCol.contains(key)) {
//					val = toLowOrUpStr(val);
//				}
//				inMap.put(key, val);
//			}
//			retList.add(inMap);
//		}
//
//		return retList;
//	}
	public static long getLongVal(String cellContents) {

		long num = 0;
		try {

			num = (long) Double.parseDouble(cellContents);
		} catch (Throwable e) {
			//CsjLog4j.logger.info(e.getMessage());
			num = 0;
		}
		return num;
	}
	public static int getIntVal(String cellContents) {

		return (int) getLongVal(cellContents);
	}
	public static double getDoubleVal(String cellContents)throws Throwable {

		double num = 0;
		try {
			if (cellContents.endsWith("%")) {
				cellContents = fromAtoBByTrim(cellContents, "", "%");
			}
			num = Double.parseDouble(cellContents);
		} catch (Throwable e) {
			throw e;
//			CsjLog4j.logger.info(e.getMessage());
//			num = 0;
		}
		return num;
	}


	public static String toLowOrUpStr(Object str) {
		return convertString(str).toUpperCase();
	}
	/**
	 * タイプ転換処理
	 *
	 * @param object
	 *        Object
	 * @return String
	 */
	public static String convertString(Object object) {

		String strResult = null;

		if (object == null) {
			strResult = "";
		} else {
			strResult = object.toString();
		}
		// 返却値を返す
		return strResult;

	}
	public static String[] getArrFromListByRegon(List<String> strList,
			boolean isHaveBlank, boolean isInFilter,String filter) {
		int blankPos = isHaveBlank ? 1 : 0;
		List<String> valList = new ArrayList<String>();
		for (String str : strList) {
			if (CsjStrUtils.isNotEmpty(filter)) {
				if (isInFilter &&str.matches(filter) ) {

				} else if (isInFilter==false &&str.matches(filter)==false ) {

				} else {
					continue;
				}

			}
			valList.add(str);
		}
		strList = valList;
		String[] s = new String[strList.size() + blankPos];

		if (isHaveBlank) {
			s[0] = "";
			for (int i = 0; i < strList.size(); i++) {

				s[i + 1] = strList.get(i);
			}
		} else {
			for (int i = 0; i < strList.size(); i++) {
				if (CsjStrUtils.isNotEmpty(filter)
						&& strList.get(i).contains(filter)) {
					continue;
				}
				s[i] = strList.get(i);
			}
		}

		return s;
	}

	/**
	 * @return
	 */
	public static String[] getArrFromList(List<String> strList,
			boolean isHaveBlank, String filter) {
		int blankPos = isHaveBlank ? 1 : 0;
		List<String> valList = new ArrayList<String>();
		for (String str : strList) {
			if (CsjStrUtils.isNotEmpty(filter) && str.contains(filter)) {
				continue;
			}
			valList.add(str);
		}
		strList = valList;
		String[] s = new String[strList.size() + blankPos];

		if (isHaveBlank) {
			s[0] = "";
			for (int i = 0; i < strList.size(); i++) {

				s[i + 1] = strList.get(i);
			}
		} else {
			for (int i = 0; i < strList.size(); i++) {
				if (CsjStrUtils.isNotEmpty(filter)
						&& strList.get(i).contains(filter)) {
					continue;
				}
				s[i] = strList.get(i);
			}
		}

		return s;
	}

	// 字符串右补位
	public static String rightFillChar(String str, char c, int length) {
		while (length > str.length()) {
			str += c;
		}
		return str;
	}

	public static String insertChar(String str, String ins,int pos) {
		String retStr = "";
		if (CsjStrUtils.isEmpty(str)) {
			return retStr;
		}
		if (str.length()<pos) {
			return str;
		}
		return str.substring(0, pos)+ins+str.substring(pos);
	}
	// 字符串左补位
	public static String leftFillChar(String str, char c, int length) {
		while (length > str.length()) {
			str = c + str;
		}
		return str;
	}

	// 字符串左补位
	public static String leftFillCharWhithByte(String str, String ch,
			long length) {
		if (isEmpty(str)) {
			str = CsjConst.EMPTY;
		}

		try {
			if (str.length() > length) {

				int len = (int) (length);
				// str = str.substring(0, len);
				str = str.substring(str.length() - len, str.length());
			}
			while (length > str.getBytes("shift-jis").length) {
				String oldStr = str;
				str = ch + str;
				if (length < str.getBytes("shift-jis").length) {
					str = " " + oldStr;
				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			e.printStackTrace();
		}
		return str;
	}

	// 字符串右补位
	public static String rightFillCharWhithByte(String str, String ch,
			long length) {
		if (isEmpty(str)) {
			str = CsjConst.EMPTY;
		}

		try {
			if (str.length() > length) {

				int len = (int) (length);
				// str = str.substring(0, len);
				str = str.substring(str.length() - len, str.length());
			}
			while (length > str.getBytes("shift-jis").length) {
				String oldStr = str;
				str = str + ch;
				if (length < str.getBytes("shift-jis").length) {
					str = oldStr + " ";
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CsjLog4j.logger.info(e.getMessage());
		}
		return str;
	}

	// 字符串右补位
	public static String rPadWhithByte(String str, String ch, String chfil,
			long length) {
		if (isEmpty(str)) {
			str = CsjConst.EMPTY;
		}

		try {
			if (str.length() > length) {

				int len = (int) (length);
				str = str.substring(str.length() - len, str.length());
				return str;
			}

			long filLength = length - str.getBytes("shift-jis").length;
			str = str + rightFillCharWhithByte(ch, chfil, filLength);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CsjLog4j.logger.info(e.getMessage());
		}
		return str;
	}

	// 字符串左补位
	public static String lPadWhithByte(String str, String ch, String chfil,
			long length) {
		if (isEmpty(str)) {
			str = CsjConst.EMPTY;
		}

		try {
			if (str.length() > length) {

				int len = (int) (length);
				str = str.substring(str.length() - len, str.length());
				return str;
			}

			long filLength = length - str.getBytes("shift-jis").length;
			str = str + leftFillCharWhithByte(ch, chfil, filLength);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CsjLog4j.logger.info(e.getMessage());
		}
		return str;
	}

	public static int getNumByte(long num) {
		int cnt = 0;
		if (num == 0) {
			return 1;
		}
		while (num != 0) {
			num = num / 10;
			cnt++;
		}
		return cnt;
	}

	public static String getLastStrBySplit(String line, String splitCh) {
		// TODO Auto-generated method stub
		String retStr = CsjConst.EMPTY;
		if (CsjStrUtils.isEmpty(line)) {
		} else if (!line.contains(splitCh)) {
		} else {
			retStr = line.substring(line.lastIndexOf(splitCh) + 1);
		}
		return retStr;
	}

	public static String trimStrByNum(String line, int num, boolean isEnd) {
		String retStr = CsjConst.EMPTY;
		if (CsjStrUtils.isEmpty(line)) {
		} else if (line.length() > num) {
			if (isEnd) {
				retStr = line.substring(0, line.length() - num);
			} else {
				retStr = line.substring(num);
			}

		}
		return retStr;
	}

	public static boolean isNumeric(String str) {
		if (isEmpty(str)) {
			return false;
		}
		str = CsjStrUtils.funReplace(str, ".", "");
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static String getNumericByStr(String str, boolean isEnd) {

		if (isEmpty(str)) {
			return CsjConst.EMPTY;
		}
		int a = 0;
		StringBuffer tmSb = new StringBuffer(str);
		StringBuffer sb = new StringBuffer();
		if (isEnd) {
			str = tmSb.reverse().toString();
		}
		int sz = str.length();

		int flg = 0;

		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i))) {
				sb.append(str.charAt(i));
				flg++;
			} else {

				if (flg != 0) {
					break;
				}

			}
		}
		if (isEnd) {
			sb.reverse();
		}
		return sb.toString();
	}

	public static long getAllNumericByStr(String str) {

		long retVal = 0;
		if (isEmpty(str)) {
			return 0;
		}

		StringBuffer sb = new StringBuffer();

		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i))) {
				sb.append(str.charAt(i));
			}
		}
		if (sb.length() == 0) {
			retVal = 0;
		} else {
			retVal = Long.parseLong(sb.toString());
		}

		return retVal;
	}

	public static String getStringAllNumericByStr(String str) {

		long retVal = 0;
		if (isEmpty(str)) {
			return CsjConst.EMPTY;
		}

		StringBuffer sb = new StringBuffer();

		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i))) {
				sb.append(str.charAt(i));
			}
		}
		if (sb.length() == 0) {
			retVal = 0;
		} else {
			retVal = Long.parseLong(sb.toString());
		}

		return String.valueOf(retVal);
	}
	public static String getAllNumABCByStr(String str, boolean isABC123) {

		if (isEmpty(str)) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (isABC123) {
				if (String.valueOf(str.charAt(i))
						.matches(RegConstStr.EN_NUM_26)) {
					sb.append(str.charAt(i));
				}
			} else {
				if (false == String.valueOf(str.charAt(i)).matches(
						RegConstStr.EN_NUM_26)) {
					sb.append(str.charAt(i));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * @param str
	 * @param signDouble
	 * @return
	 */
	public static String addLRSign(String str, String signDouble) {
		// TODO Auto-generated method stub
		String retStr = CsjConst.EMPTY;
		if (CsjStrUtils.isEmpty(str)) {
			retStr = CsjConst.EMPTY;
		} else {
			retStr = signDouble + str + signDouble;
		}
		return retStr;
	}

	/**
	 * @param str
	 * @param signDouble
	 * @return
	 */
	// TODO CSJ
	public static HashMap<String, String> getSplitMap(String str,
			String bSplit, String sSplit, Set<String> trimChSet,boolean isHead) {
		HashMap<String, String> retMap = new HashMap<String, String>();
		if (CsjStrUtils.isNotEmpty(str)) {
			String[] strBArr = str.split(bSplit);
			for (String strB : strBArr) {
				if (strB.contains(sSplit)) {
					String[] strSArr = strB.split(sSplit);
					if (strSArr != null && strSArr.length >= 2) {
						String key = "";
						String val = "";
						if (strSArr.length == 1) {
							key = strSArr[0];
						} else if (strSArr.length >= 2) {
							key = strSArr[0];
							val = strSArr[1];
						}
						val = CsjStrUtils.lrTrimStartEndBySet(val, trimChSet,isHead);
						retMap.put(key, val);
					}
				}
			}
		}

		return retMap;
	}

	public static InputStream String2ByteArrayInputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	public static InputStream String2InputStreasm(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	public static String inputStream2String(InputStream is) throws Throwable {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	/**
	 * @param oldContent
	 * @param paraLen
	 * @return
	 */
	public static String getStrForLength(String str, long paraLen) {
		try {
			if (CsjStrUtils.isEmpty(str)) {
				return CsjConst.EMPTY;
			} else {
				while (true) {
					if (str.getBytes(CsjFileConst.ENCODE_SHIFT_JIS).length <= paraLen) {

						break;
					} else {
						str = str.substring(0, str.length() - 1);
					}
				}

			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CsjLog4j.logger.info(e.getMessage());
		}
		// TODO Auto-generated method stub
		return str;
	}

	public static String getStrForFileNm(String str) {

		String retStr = CsjConst.EMPTY;
		StringBuffer sb = new StringBuffer();
		if (isNotEmpty(str)) {
			char[] chArr = str.toCharArray();

			for (int i = 0; i < chArr.length; i++) {
				String strTmp = String.valueOf(chArr[i]);
				if (strTmp.matches(RegConstStr.ERROR_FILE_NM)) {
					sb.append(strTmp);
				}
			}
			retStr = sb.toString();
		}
		return retStr;

	}


	public static String getStringBySign(Set<String> set,String split) {
		String ret = "";
		for (String s : set) {
			ret+=s;
		}

		return ret;
	}
	/**
	 * @param dbMap
	 * @param key
	 */
	public static String getMapValForConvert(HashMap<String, String> map, String key) {
		if (isEmpty(map) || map.containsKey(key) == false) {
			return key;
		} else {
			return map.get(key);
		}
	}
	/**
	 * @param dbMap
	 * @param key
	 */
	public static HashSet<String> getSet(String str, String regex, boolean isWithBlank) {

		HashSet<String> ret = new HashSet<String>();
		if (isEmpty(str)) {
			return ret;
		}
		for (String s : str.split(regex)) {
			if (isWithBlank) {
				ret.add(s);
			} else {
				if (isNotEmpty(s)) {
					ret.add(s);
				}
			}
		}
		return ret;
	}

	/**
	 * @param createTblSqlList
	 */
	public static String getStrByList(List<String> strList) {
		StringBuffer sb = new StringBuffer();
		if (CsjStrUtils.isNotEmpty(strList)) {
			for(String str:strList) {
				sb.append(str);
			}
		}
		return sb.toString();
	}
	
	/**
	 * @param createTblSqlList
	 */
	public static String getStrByListWithCr(List<String> strList, boolean isHeadCr) {
		StringBuffer sb = new StringBuffer();
		
		if (CsjStrUtils.isNotEmpty(strList)) {
			if (isHeadCr) {
				sb.append(CsjProcess.s_newLine);
			}
			for(String str:strList) {
				sb.append(str);
			}
		}
		return sb.toString();
	}
	
	/**
	 * @param retStr
	 * @return
	 */
	public static String getNumberByTrimDot0(String str) {
		if (CsjStrUtils.isNotEmpty(str)) {
			if (str.matches(RegConstStr.NUMBER_INTEGER)) {
				str = String.valueOf(CsjStrUtils.getIntVal(str));
			}
		}
		return str;
	}
	/**
	 * @param equalSet
	 * @param val
	 * @return
	 */
	public static boolean isNumInSet(Set<String> equalSet, String val) throws Throwable{
		boolean retVal = false;
		try {
			BigDecimal bval = new BigDecimal(val);
			for (String str : equalSet) {
				if (bval.equals(new BigDecimal(str))) {
					retVal = true;
				}
			}
		} catch (Throwable e) {
			throw e;
		}
		return retVal;
	}
	/**
	 * @param val
	 * @return
	 */
	public static boolean isXlsDateStr(String val) {

		boolean isXlsDateStr = true;
		try {
			val = val.replaceAll("-", "/");
			String format = CsjConst.EMPTY;
			if (val.getBytes(CsjFileConst.ENCODE_SHIFT_JIS).length > 10) {
				format = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24;

			} else if (val.getBytes(CsjFileConst.ENCODE_SHIFT_JIS).length > 19) {
				format = CsjConst.YYYY_MM_DD_HH_MM_SS_SLASH_24_SSS;
			} else {
				format = CsjConst.YYYY_MM_DD_SLASH;
			}
			isXlsDateStr = CsjDateUtil.checkDate(val, format);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CsjLog4j.logger.debug(e.getMessage());
			isXlsDateStr = false;
		}

		return isXlsDateStr;
	}
	/**
	 * @param strDate
	 * @return
	 */
	public static String getExcelDate(String strDate) {
		strDate = CsjStrUtils.fromAtoBByTrim(strDate, "", " 00:00:00.0");
		return strDate;
	}

	public static String subStr(String str, int num) {
		
		String retStr = "";
		if (CsjStrUtils.isEmpty(str)) {
			return retStr;
		} 
		if (str.length()<num) {
			return str;
		} else {
			return str.substring(0, num);
		}
	}
	public static void main(String[] args) {
		System.out.println(getAllNumericByStr("abc1a"));
	}

	public static String trimLeftChar(String retVal, String trimCh) {
		if (isEmpty(retVal)) {
			return CsjConst.EMPTY;
		}
		while (retVal.startsWith(trimCh)) {
			retVal = retVal.substring(1);
		}
		return retVal;
	}

}
