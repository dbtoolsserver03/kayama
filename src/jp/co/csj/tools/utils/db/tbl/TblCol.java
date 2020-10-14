package jp.co.csj.tools.utils.db.tbl;

public class TblCol {
	private String colNmEn="";
	private String colNmJp="";
	private String colType="";
	private String comment="";
	private int length=0;
	private int scale=0;
	private boolean isKey;
	private boolean canNull;
	public String getColNmEn() {
		return colNmEn;
	}
	public void setColNmEn(String colNmEn) {
		this.colNmEn = colNmEn;
	}
	public String getColNmJp() {
		return colNmJp;
	}
	public void setColNmJp(String colNmJp) {
		this.colNmJp = colNmJp;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public boolean isKey() {
		return isKey;
	}
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
	public boolean isCanNull() {
		return canNull;
	}
	public void setCanNull(boolean canNull) {
		this.canNull = canNull;
	}
	
	
	
}
