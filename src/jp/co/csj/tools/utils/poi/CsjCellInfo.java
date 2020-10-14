/**
 *
 */
package jp.co.csj.tools.utils.poi;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import jp.co.csj.tools.utils.constant.CsjConst;

/**
 * @author 963210
 *
 */
public class CsjCellInfo {

	private String content = CsjConst.EMPTY;
	private int cellNum = -1;
	private int rowNum = -1;
	private CellStyle cs = null;
	private Font font = null;
	private String sheetNm = "";

	/**
	 *
	 */
	public CsjCellInfo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param content
	 * @param cellNum
	 * @param rowNum
	 */
	public CsjCellInfo(String content, int rowNum,int cellNum) {
		this.content = content;
		this.rowNum = rowNum;
		this.cellNum = cellNum;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the cellNum
	 */
	public int getCellNum() {
		return cellNum;
	}

	/**
	 * @param cellNum the cellNum to set
	 */
	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}

	/**
	 * @return the rowNum
	 */
	public int getRowNum() {
		return rowNum;
	}

	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjCellInfo [content=" + content + ", cellNum=" + cellNum
				+ ", rowNum=" + rowNum + ", cs=" + cs + ", font=" + font + "]";
	}

	/**
	 * @return the sheetNm
	 */
	public String getSheetNm() {
		return sheetNm;
	}

	/**
	 * @param sheetNm the sheetNm to set
	 */
	public void setSheetNm(String sheetNm) {
		this.sheetNm = sheetNm;
	}

	/**
	 * @return the cs
	 */
	public CellStyle getCs() {
		return cs;
	}

	/**
	 * @param cs the cs to set
	 */
	public void setCs(CellStyle cs) {
		this.cs = cs;
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}

}
