package jp.co.csj.tools.utils.db.tbl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TblInfo {

	
	public static final String TBL_NM_JP = "TBL_NM_JP";
	public static final String TBL_NM_EN = "TBL_NM_EN";
	public static final String COL_ID = " ";//No.
	public static final String COL_NM_JP = "COL_NM_JP";
	public static final String COL_NM_EN = "COL_NM_EN";
	public static final String COL_TYPE_INFO = "COL_TYPE_INFO";
	public static final String COL_IS_PK = "COL_IS_PK";
	public static final String COL_CAN_NULL = "COL_CAN_NULL";
	public static final String COL_DEFAULT = "COL_DEFAULT";
	public static final String COL_TYPE = "COL_TYPE";
	public static final String COL_LENGTH = "COL_LENGTH";
	public static final String COL_EXTRA = "EXTRA";
	
	
	private String tblNmEn="";
	private String tblNmJp="";
	private Set<String> keySet = new TreeSet<>();
	private Map<String, TblCol> tblColMap = new HashMap<>();
	private List<Map<String,Object>> dataList = new ArrayList<>();
	public String getTblNmEn() {
		return tblNmEn;
	}
	public void setTblNmEn(String tblNmEn) {
		this.tblNmEn = tblNmEn;
	}
	public String getTblNmJp() {
		return tblNmJp;
	}
	public void setTblNmJp(String tblNmJp) {
		this.tblNmJp = tblNmJp;
	}
	public Set<String> getKeySet() {
		return keySet;
	}
	public void setKeySet(Set<String> keySet) {
		this.keySet = keySet;
	}
	public Map<String, TblCol> getTblColMap() {
		return tblColMap;
	}
	public void setTblColMap(Map<String, TblCol> tblColMap) {
		this.tblColMap = tblColMap;
	}
	public List<Map<String, Object>> getDataList() {
		return dataList;
	}
	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}
	
	
	
}
