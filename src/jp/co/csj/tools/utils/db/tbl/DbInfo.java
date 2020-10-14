package jp.co.csj.tools.utils.db.tbl;

import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.csj.tools.utils.db.JdbcConnectionManager;

public class DbInfo {

	private JdbcConnectionManager jcm;
	private Map<String, TblInfo> tblInfoMap = new LinkedHashMap<>();

	public Map<String, TblInfo> getTblInfoMap() {
		return tblInfoMap;
	}

	public void setTblInfoMap(Map<String, TblInfo> tblInfoMap) {
		this.tblInfoMap = tblInfoMap;
	}

	public JdbcConnectionManager getJcm() {
		return jcm;
	}

	public void setJcm(JdbcConnectionManager jcm) {
		this.jcm = jcm;
	}
	
}
