/**
 * 
 */
package jp.co.csj.tools.utils.poi;

import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author csj
 *
 */
public class CsjPatr {
    private Drawing patr = null;
    private short col1 = 0;
    private int row1 = 0;
    private short col2 = 0;
    private int row2 = 0;
    /**
     * @return the patr
     */
    public Drawing getPatr() {
        return patr;
    }
    /**
     * @param patr
     * @param col1
     * @param row1
     * @param col2
     * @param row2
     */
    public CsjPatr(Sheet sheet, int col1, int row1, int col2, int row2) {
        this.patr = sheet.createDrawingPatriarch();
        this.col1 = (short)col1;
        this.row1 = row1;
        this.col2 = (short)col2;
        this.row2 = row2;
    }
    /**
     * @param patr the patr to set
     */
    public void setPatr(Drawing patr) {
        this.patr = patr;
    }
    /**
     * @return the col1
     */
    public short getCol1() {
        return col1;
    }
    /**
     * @param col1 the col1 to set
     */
    public void setCol1(short col1) {
        this.col1 = col1;
    }
    /**
     * @return the row1
     */
    public int getRow1() {
        return row1;
    }
    /**
     * @param row1 the row1 to set
     */
    public void setRow1(int row1) {
        this.row1 = row1;
    }
    /**
     * @return the col2
     */
    public short getCol2() {
        return col2;
    }
    /**
     * @param col2 the col2 to set
     */
    public void setCol2(short col2) {
        this.col2 = col2;
    }
    /**
     * @return the row2
     */
    public int getRow2() {
        return row2;
    }
    /**
     * @param row2 the row2 to set
     */
    public void setRow2(int row2) {
        this.row2 = row2;
    }
    
}
