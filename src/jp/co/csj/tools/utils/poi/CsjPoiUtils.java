
package jp.co.csj.tools.utils.poi;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.csj.tools.utils.constant.CsjConst;
import jp.co.csj.tools.utils.date.CsjDateUtil;
import jp.co.csj.tools.utils.str.CsjStrUtils;


public class CsjPoiUtils {

	public static final int S_CELL_MAX_NUM_2007 = 1048576;
	public static final int S_CELL_MAX_NUM_2003 = 32767;

	public static void main(String[] args) throws Throwable {
		
			Workbook wb = getWorkBook("tmp\\tmp.xlsx");
			Sheet st = wb.getSheetAt(0);
			for (Row row : st) {
				for (Cell cell : row) {
					System.out.println(getCellContent(cell, true));
				}
			}
	}
	
	
	 /**
	 * Remove a row by its index
	 * @param sheet a Excel sheet
	 * @param rowIndex a 0 based index of removing row
	 * @throws Throwable 
	 */
	public static void removeRow(Sheet sheet, int rowIndex) throws Throwable {
		    int lastRowNum=sheet.getLastRowNum();
		    if(rowIndex>=0&&rowIndex<lastRowNum)
		        sheet.shiftRows(rowIndex+1,lastRowNum,-1);//将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
		    if(rowIndex==lastRowNum){
		        Row removingRow=sheet.getRow(rowIndex);
		        if(removingRow!=null)
		            sheet.removeRow(removingRow);
		    }

	}
	
	public static void insertRow(Sheet sheet, int starRow, int rows,boolean isWithContent) throws Throwable {

			if (rows <= 0) {
				return;
			}
			if (starRow + 1 == sheet.getLastRowNum()) {
				CsjPoiUtils.setCellValue(sheet, starRow + 2, 0, " ");
			}
			// 选择一个区域，从startRow+1直到最后一行
			sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);

			starRow = starRow - 1;

			for (int i = 0; i < rows; i++) {
				Row sourceRow = null;
				Row targetRow = null;
				Cell sourceCell = null;
				Cell targetCell = null;
				int m;
				starRow = starRow + 1;
				sourceRow = sheet.getRow(starRow);
				if (sourceRow == null) {
					sourceRow = sheet.createRow(starRow);
				}
				// 從start創建新的一行
				targetRow = sheet.createRow(starRow + 1);
				targetRow.setHeight(sourceRow.getHeight());

				// 处理刚刚创建的一行
				for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++) {
					sourceCell = sourceRow.getCell(m);
					targetCell = targetRow.createCell(m);

					if (sourceCell!=null &&targetCell!=null) {
						// 风格一样
						targetCell.setCellStyle(sourceCell.getCellStyle());
						targetCell.setCellType(sourceCell.getCellType());
						if (isWithContent) {
							targetCell.setCellValue(getCellStr(sourceCell));
						}
					}
				}
			}
	}

	public static Cell setCellValueWithCs(Sheet sheet, int rowPos, int colNum, String str, CellStyle cs) throws Throwable {
		Cell cell = null;
			cell = setCellValue(sheet, rowPos, colNum, str);
			if (cs != null) {
				cell.setCellStyle(cs);
				cell.setCellValue(str);
			}

		return cell;
	}
	public static Cell setCellValueWithCs(Sheet sheet, int rowPos, int colNum, String str, CellStyle cs, CellStyle csBlank) throws Throwable {
		Cell cell = null;
			cell = setCellValue(sheet, rowPos, colNum, str);
			if (CsjStrUtils.isEmpty(str)) {
				cell.setCellStyle(csBlank);
			} else {
				cell.setCellStyle(cs);
			}
		return cell;
	}

	public static Cell setCellValueWithCs(Sheet sheet, int rowPos, int colNum, RichTextString str,
			CellStyle cs) throws Throwable {
		
		Cell cell = null;
			cell = setCellValue(sheet, rowPos, colNum, str);
			if (cell != null) {
				cell.setCellStyle(cs);
			}
			

		return cell;
	}

	public static Cell setCellValue(Sheet sheet, int rowNum, int colNum, RichTextString val) throws Throwable {
		
		Cell cell = null;
			cell = getOrCreateCell(sheet, rowNum, colNum);

			if (CsjStrUtils.isNotEmpty(val) && getExcelMaxRowNum(sheet) < val.length()) {
				throw new Exception("xxxxxxxxxxxxxxxxxxxxx");
			}
			if (cell != null) {
				cell.setCellValue(val);
			}
			
		return cell;
	}
	public static Cell setCellValue(Sheet sheet, int rowNum, int colNum, String val) throws Throwable {
		Cell cell = null;
			cell = getOrCreateCell(sheet, rowNum, colNum);
			if (CsjStrUtils.isNotEmpty(val) && getExcelMaxRowNum(sheet) < val.length()) {
				throw new Exception("xxxxxxxxxxxxxxxxxxxxxxx");
			}
			if (cell != null) {
				cell.setCellValue(val);
			}
			

		return cell;
	}
	private static int getExcelMaxRowNum(Sheet sheet) throws Throwable{

		int maxRow = 0;
		
			if (sheet instanceof HSSFSheet) {
				maxRow = S_CELL_MAX_NUM_2003;
			} else {
				maxRow = S_CELL_MAX_NUM_2007;
			}
	
		return maxRow;
	}


	/**
	 * @param sheet
	 * @param rowNum
	 * @param colNum
	 * @return
	 */
	public static Cell getOrCreateCell(Sheet sheet, int rowNum, int colNum) throws Throwable {
		Cell cell = null;
			if (sheet == null) {
				return null;
			}
			
			Row row = sheet.getRow(rowNum);
			if (row == null) {
				row = sheet.createRow(rowNum);
			}
			cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
			}

		return cell;
	}

	public static List<String>
			getCellContents(File inFile, String fromSheetName) throws Throwable {
		List<String> retList = new ArrayList<String>();

			Workbook wb =getWorkBook(inFile.getAbsolutePath());

			Sheet sheet = wb.getSheet(fromSheetName);
			String str = CsjConst.EMPTY;
			for (Row row : sheet) {
				for (Cell cell : row) {
					str = getCellContent( cell, false);
					retList.add(str);
				}
			}

		return retList;
	}


	public static List<String> getCellContents(File inFile,
			String fromSheetName, int rowNum) throws Throwable {
		List<String> retList = new ArrayList<String>();

			Workbook wb =getWorkBook(inFile.getAbsolutePath());

			Sheet sheet = wb.getSheet(fromSheetName);
			String str = CsjConst.EMPTY;
			for (Row row : sheet) {
				if (row.getRowNum() == rowNum) {
					for (Cell cell : row) {
						str = getCellContent( cell, false);
						retList.add(str);
					}
				}
			}

		return retList;
	}

	
	public static List<String> getCellContents(Row row, boolean checkStrikeOut) throws Throwable  {

		List<String> retList = new ArrayList<String>();

			if (row != null) {
				String str = CsjConst.EMPTY;
				for (Cell cell : row) {

					str = getCellContent( cell, checkStrikeOut);
					if (CsjStrUtils.isNotEmpty(str)) {
						retList.add(str);
					}
				}
			}

		return retList;
	}
	public static List<String> getCellContents(Row row, int colBegin,boolean checkStrikeOut) throws Throwable {

		List<String> retList = new ArrayList<String>();

			if (row != null) {
				String str = CsjConst.EMPTY;
				for (Cell cell : row) {
					if (cell.getColumnIndex()<colBegin) {
						continue;
					}
					str = getCellContent( cell, checkStrikeOut);
					if (CsjStrUtils.isNotEmpty(str)) {
						retList.add(str);
					}
				}
			}


		return retList;
	}
	public static CsjCellInfo getCellFontByContent(Row row, String str,boolean checkStrikeOut) throws Throwable {

		CsjCellInfo cellInfo =null;
			if (row != null) {
				for (Cell cell : row) {
					if (str.equals(getCellContent(cell, checkStrikeOut))) {
						cellInfo = getCellInfo(cell);
						break;
					}
				}
			}

		return cellInfo;
	}
	public static Font createCellFont(Workbook wb, CsjCellInfo cellInfo) {
		Font ft = wb.createFont();
		
		Font f = cellInfo.getFont();
		ft.setBold(f.getBold());
		ft.setBold(f.getBold());
		ft.setCharSet(f.getCharSet());
		ft.setColor(f.getColor());
		ft.setFontHeight(f.getFontHeight());
		ft.setFontHeightInPoints(f.getFontHeightInPoints());
		ft.setFontName(f.getFontName());
		ft.setItalic(f.getItalic());
		ft.setStrikeout(f.getStrikeout());
		ft.setTypeOffset(f.getTypeOffset());
		ft.setUnderline(f.getUnderline());
		return ft;
	}
	public static CellStyle createCellStyle(Workbook wb,CsjCellInfo cellInfo) {
		CellStyle retCs = wb.createCellStyle();
		
		if (cellInfo == null) {
			return null;
		}
		CellStyle cs =cellInfo.getCs();
		if (cs == null) {
			return null;
		}
		retCs.setAlignment(cs.getAlignment());
		retCs.setBorderBottom(cs.getBorderBottom());
		retCs.setBorderLeft(cs.getBorderLeft());
		retCs.setBorderRight(cs.getBorderRight());
		retCs.setBorderTop(cs.getBorderTop());
		retCs.setBottomBorderColor(cs.getBottomBorderColor());
		retCs.setDataFormat(cs.getDataFormat());
		retCs.setFillBackgroundColor(cs.getFillBackgroundColor());
		retCs.setFillForegroundColor(cs.getFillForegroundColor());
		retCs.setFillPattern(cs.getFillPattern());
		retCs.setFont(createCellFont(wb, cellInfo));
		retCs.setHidden(cs.getHidden());
		retCs.setIndention(cs.getIndention());
		retCs.setLeftBorderColor(cs.getLeftBorderColor());
		retCs.setLocked(cs.getLocked());
		retCs.setRightBorderColor(cs.getRightBorderColor());
		retCs.setRotation(cs.getRotation());
		retCs.setShrinkToFit(cs.getShrinkToFit());
		retCs.setTopBorderColor(cs.getTopBorderColor());
		retCs.setVerticalAlignment(cs.getVerticalAlignment());
		retCs.setWrapText(cs.getWrapText());


//		if (cell instanceof HSSFCell) {
//			HSSFCell c = (HSSFCell)cell;
//			font = c.getCellStyle().getFont(c.getRow().getSheet().getWorkbook());
//		} else {
//			XSSFCell c = (XSSFCell)cell;
//			font = c.getCellStyle().getFont();
//		}
//		
//		Font font = ((HSSFCellStyle)retCs).getFont((HSSFWorkbook)wb);
//		font.getBold();

		
		return retCs;
	}
	public static void testgetCellContents(File inFile, String fromSheetName,
			int rowNum) throws Throwable {

			Workbook wb =getWorkBook(inFile.getAbsolutePath());
			

			Sheet sheet = wb.getSheet(fromSheetName);
			removeRow(sheet, rowNum);

			FileOutputStream fileOut = new FileOutputStream("d:\\aaa1.xls");
			wb.write(fileOut);
			fileOut.close();

	}

	public static LinkedHashMap<Integer,String> getCellContentMaps(Row row, boolean checkStrikeOut) throws Throwable {

		LinkedHashMap<Integer,String> map = new LinkedHashMap<Integer, String>();
			if (row != null) {
				String str = CsjConst.EMPTY;
				for (Cell cell : row) {

					str = getCellContent( cell, checkStrikeOut);

					if (CsjStrUtils.isNotEmpty(str)) {
						map.put(cell.getRowIndex(), str);
					}
				}
			}
		return map;
	}
	public static List<String> getCellContents(Sheet sheet, int col, int rowMin,int rowMax, boolean checkStrikeOut) throws Throwable {

		List<String> retList = new ArrayList<String>();

			String str = CsjConst.EMPTY;
			for (Row row:  sheet) {
				int rowNum = row.getRowNum();
				if (rowMin<=rowNum&& rowNum<=rowMax) {
					str = getCellContent(row, col, checkStrikeOut);
					retList.add(str);
				}
			}

		return retList;
	}
	public static List<String> getCellContents(Row row, int preCol, int endCol, boolean checkStrikeOut) throws Throwable {

		List<String> retList = new ArrayList<String>();
			String str = CsjConst.EMPTY;
			for (Cell cell : row) {
				int cellCol = cell.getColumnIndex();
				if (preCol <= cellCol && cellCol <= endCol) {
					str = getCellContent( cell, checkStrikeOut);
					retList.add(str);
				}
			}

		return retList;
	}
	public static LinkedHashMap<String,String> getCellContentsMap(Row row, boolean checkStrikeOut) throws Throwable {

		LinkedHashMap<String,String> retMap = new LinkedHashMap<String,String>();

			String str = CsjConst.EMPTY;
			for (Cell cell : row) {

				str = getCellContent( cell, checkStrikeOut);
				if (CsjStrUtils.isNotEmpty(str)) {
					retMap.put(String.valueOf(cell.getColumnIndex()), str);
				}
			}

		return retMap;
	}
	public static String getCellContent(Row row, int colIndex, boolean checkStrikeOut) throws Throwable {

		String retStr = CsjConst.EMPTY;
			if (row == null) {
				return CsjConst.EMPTY;
			}
	
			Cell cell = row.getCell(colIndex);
			if(cell == null) {
				return CsjConst.EMPTY;
			}
			retStr = getCellContent(cell, checkStrikeOut);
			
		return retStr;
	}
	public static List<String> getCellContents(Sheet sheet, boolean checkStrikeOut) throws Throwable {

		List<String> retList = new ArrayList<String>();


			String str = CsjConst.EMPTY;
			for (Row row : sheet) {
				for (Cell cell : row) {

					str = getCellContent( cell, checkStrikeOut);
					retList.add(str);
				}
			}


		return retList;
	}
	public static void clearCellContents(Sheet sheet,int rowMin,int rowMax,int colMin,int colMax) {

			for (Row row : sheet) {

				if (row.getRowNum()<=rowMin||(rowMax>=0 && row.getRowNum()>=rowMax)) {
					continue;
				}
				for (Cell cell : row) {
					if (cell.getColumnIndex()<=colMin||(colMax>=0 && cell.getColumnIndex()>=colMax)) {
						continue;
					}
					cell.setCellValue("");
				}
			}

	}
	public static HashSet<String> getCellContentsMap(String filePath, String sheetNm,boolean checkStrikeOut)throws Throwable {
		return getCellContentsMap(new File(filePath),sheetNm,checkStrikeOut);
	}
	public static HashSet<String> getCellContentsMap(File f, String sheetNm, boolean checkStrikeOut)throws Throwable {
		HashSet<String> sSet = new HashSet<String>();
		
			Workbook wb =getWorkBook(f.getAbsolutePath());
			String str = CsjConst.EMPTY;
			
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i);
		
				if (CsjStrUtils.isNotEmpty(sheetNm)) {
					if (sheet.getSheetName().equals(sheetNm) == false) {
						continue;
					}
				}
				for (Row row : sheet) {
					for (Cell cell : row) {
		
						str = CsjStrUtils.lrTrimSpace(getCellContent( cell, false));
						if (CsjStrUtils.isNotEmpty(str)) {
							sSet.add(CsjStrUtils.toLowOrUpStr(str));
						}
		
					}
				}
			}
		
		return sSet;
	}
	
	public static List<String> getCellContents(String filePath, boolean checkStrikeOut) throws Throwable {

		List<String> retList = new ArrayList<String>();
			File f = new File(filePath);
			if (f.isFile() == false) {
				return retList;
			}
			Workbook wb =getWorkBook(filePath);
			String str = CsjConst.EMPTY;

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i);
				for (Row row : sheet) {
					for (Cell cell : row) {
						str = getCellContent( cell, false);
						retList.add(str);
					}
				}
			}
		return retList;
	}

	public static String getCellContent(Sheet sheet, int rowIndex, int colIndex, boolean checkStrikeOut) throws Throwable {

		String retVal = CsjConst.EMPTY;
			if (sheet != null) {
				Row row = sheet.getRow(rowIndex);

				retVal = getCellContent(row, colIndex, checkStrikeOut);
			}
		return retVal;
	}
	public static String getCellContent(String fileAbsPath,String sheetNm, int rowIndex, int colIndex, boolean checkStrikeOut) throws Throwable {
		FileInputStream fileIn = null;
		Workbook wb;
		try {
			 wb =getWorkBook(fileAbsPath);
		} catch (Throwable e) {
			throw e;
		} finally {
			if (fileIn != null) {
				fileIn.close();
			}
		}
		return getCellContent(wb.getSheet(sheetNm),rowIndex, colIndex, checkStrikeOut);

	}
	public static String getCellAreaContents(Sheet sheet, int rowBeginIndex, int rowEndIndex, int colBeginIndex,
			int colEndIndex, List<String> strList, boolean checkStrikeOut) throws Throwable {

		StringBuffer retSb = new StringBuffer();

			for (Row row : sheet) {
				if (rowBeginIndex <= row.getRowNum() && row.getRowNum() <= rowEndIndex) {
					for (Cell cell : row) {

						if (colBeginIndex <= cell.getColumnIndex() && cell.getColumnIndex() <= colEndIndex) {
							String val = getCellContent( cell, checkStrikeOut);
							retSb.append(val);
							strList.add(val);
						}
					}
				}
			}


		return retSb.toString();
	}

	public static String getCellContent(Cell cell, boolean checkStrikeOut) throws Throwable {

		String retStr = CsjConst.EMPTY;
			if (null == cell) {
				return retStr;
			}
			if (checkStrikeOut) {
				Font font;
				if (cell instanceof HSSFCell) {
					HSSFCell c = (HSSFCell)cell;
					font = c.getCellStyle().getFont(c.getRow().getSheet().getWorkbook());
					if (font.getStrikeout()) {
						return retStr;
					}
				} else if (cell instanceof XSSFCell) {
					XSSFCell c = (XSSFCell)cell;
					font = c.getCellStyle().getFont();
					if (font.getStrikeout()) {
						return retStr;
					} 
				}
			}
			retStr = getCellStr(cell);
		
		return retStr;
	}

	public static CsjCellInfo getCellInfo(Cell cell) throws Throwable {

		if (null == cell) {
			return null;
		}
		CsjCellInfo retVal = null;
			retVal = new CsjCellInfo(getCellStr(cell),cell.getRowIndex(),cell.getColumnIndex());
			Font font = null;
			if (cell instanceof HSSFCell) {
				HSSFCell c = (HSSFCell)cell;
				font = c.getCellStyle().getFont(c.getRow().getSheet().getWorkbook());
			} else {
				XSSFCell c = (XSSFCell)cell;
				font = c.getCellStyle().getFont();
			}
			
			CellStyle cs = cell.getCellStyle();

			retVal.setCs(cs);
			retVal.setFont(font);
		return retVal;
	}
	/**
	 * @param cell
	 * @param retStr
	 * @return
	 * @throws Throwable 
	 */
	private static String getCellStr(Cell cell) throws Throwable {
		
		String retStr = "";
			switch (cell.getCellType()) {
			case STRING:
				retStr = cell.getRichStringCellValue().getString();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					retStr = CsjDateUtil.getFormatDateTime(cell.getDateCellValue(), CsjConst.YYYY_MM_DD_SLASH);
				} else {
					retStr = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case BOOLEAN:
				retStr = String.valueOf(cell.getBooleanCellValue());
				break;
			case FORMULA:
				try {
					if (DateUtil.isCellDateFormatted(cell)) {
						retStr = CsjDateUtil.getFormatDateTime(cell.getDateCellValue(), CsjConst.YYYY_MM_DD_SLASH);
					} else {
						retStr = String.valueOf(cell.getNumericCellValue());
						retStr = CsjStrUtils.getNumberByTrimDot0(retStr);
					}
				} catch (Throwable e) {
					try {
						retStr = cell.getRichStringCellValue().getString();
					} catch (Throwable e1) {
						try {
							retStr = CsjDateUtil.getFormatDateTime(cell.getDateCellValue(), CsjConst.YYYY_MM_DD_SLASH);
						} catch (Throwable e2) {
							retStr = cell.getCellFormula();
						}
					}
				}
				break;
			default:
				retStr = String.valueOf(cell.getStringCellValue());
			}
		
		return retStr;
	}

	
	public static void setCellComment(Cell cell, RichTextString content, String author) throws Throwable {
			// Create the drawing patriarch. This is the top level container for all shapes including cell comments.
			Drawing patr = cell.getSheet().createDrawingPatriarch();
			
			//anchor defines size and position of the comment in worksheet
			
			Comment comment = null;
			if (cell instanceof HSSFCell) {
				comment =  patr.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 6, 5));

				// set text in the comment
				comment.setString(content);
			} else if (cell instanceof XSSFCell) {
				comment =  patr.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 6, 5));
				XSSFRichTextString xssfRichTextString = new XSSFRichTextString(content.getString());
				comment.setString(xssfRichTextString);
			}
			
			//set comment author.
			//you can see it in the status bar when moving mouse over the commented cell
			comment.setAuthor(author);


			// The first way to assign comment to a cell is via XSSFCell.setCellComment method
			cell.setCellComment(comment);

	}
	public static void setCellCommentBig(Cell cell, RichTextString content, String author) throws Throwable {
			// Create the drawing patriarch. This is the top level container for all shapes including cell comments.
			Drawing patr = cell.getSheet().createDrawingPatriarch();
			//anchor defines size and position of the comment in worksheet
			
			Comment comment = null;
			if (cell instanceof HSSFCell) {
				comment =  patr.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 8, 17));
				// set text in the comment
				comment.setString(content);
			} else if (cell instanceof XSSFCell) {
				comment =  patr.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 8, 17));
				XSSFRichTextString xssfRichTextString = new XSSFRichTextString(content.getString());
				comment.setString(xssfRichTextString);
			}

			//set comment author.
			//you can see it in the status bar when moving mouse over the commented cell
			comment.setAuthor(author);

			// The first way to assign comment to a cell is via XSSFCell.setCellComment method
			cell.setCellComment(comment);

	}
	/**
	 * @param wb
	 * @param csjPart
	 * @throws Throwable
	 */
	public static void setCommentByCell(Cell cell, String content, Font font, CsjPatr csjPart,
			boolean isVisable, String user) throws Throwable {
		// Create the drawing patriarch. This is the top level container for all
		// shapes including cell comments.
			
			Comment comment = null;
			RichTextString string;
			if (cell instanceof HSSFCell) {
				comment =  csjPart.getPatr().createCellComment(
						new HSSFClientAnchor(0, 0, 0, 0, csjPart.getCol1(), csjPart.getRow1(), csjPart.getCol2(), csjPart
								.getRow2()));
				 string = new HSSFRichTextString(content);
				 
					string.applyFont(font);
					comment.setString(string);
			} else if (cell instanceof XSSFCell) {
				comment =  csjPart.getPatr().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, csjPart.getCol1(), csjPart.getRow1(), csjPart.getCol2(), csjPart
								.getRow2()));
				 string = new XSSFRichTextString(content);
					string.applyFont(font);
					comment.setString(string);
			}
			// modify background color of the comment
			//comment.setFillColor(204, 236, 255);


			comment.setVisible(isVisable); // by default comments are hidden. This
											// one is always visible.
			if (CsjStrUtils.isNotEmpty(user)) {
				comment.setAuthor(user);
			}

			/**
			 * The second way to assign comment to a cell is to implicitly specify
			 * its row and column. Note, it is possible to set row and column of a
			 * non-existing cell. It works, the comment is visible.
			 */
			comment.setRow(cell.getRowIndex());
			comment.setColumn(cell.getColumnIndex());

	}

	public static void setColorForDif(RichTextString oldRichStr,
			RichTextString newRichStr, Font sFontRed, String enCode) throws Throwable {

		String oldStr = oldRichStr.getString();
		String newStr = newRichStr.getString();
		List<CsjPosition> oldPositionList = new ArrayList<CsjPosition>();
		List<CsjPosition> newPositionList = new ArrayList<CsjPosition>();
			byte[] oldByteArr = oldStr.getBytes(enCode);
			byte[] newByteArr = newStr.getBytes(enCode);

			int oldLen = oldStr.length();//oldByteArr.length;
			int newLen = newStr.length();//ByteArr.length;
			int minLen = oldLen<newLen? oldLen : newLen;

			for (int i = 0; i < minLen; i++) {
				if (oldStr.charAt(i) != newStr.charAt(i)) {
					oldPositionList.add(new CsjPosition(i, i+1));
					newPositionList.add(new CsjPosition(i, i+1));
				}
			}
			if (minLen < oldLen) {
				oldPositionList.add(new CsjPosition(minLen, oldLen));
			} else if (minLen < newLen) {
				newPositionList.add(new CsjPosition(minLen, newLen));
			}

			for (CsjPosition pos : oldPositionList) {
				oldRichStr.applyFont(pos.getStartPos(), pos.getEndPos(), sFontRed);
			}
			for (CsjPosition pos : newPositionList) {
				newRichStr.applyFont(pos.getStartPos(), pos.getEndPos(), sFontRed);
			}
	}

	/*
	 * /**
	 *
	 * @param logger
	 *
	 * @param sheet
	 */
	public static void printSheet(String path, Sheet sheet) throws Throwable {
			for (Row row : sheet) {
				for (Cell cell : row) {
					String val = getCellContent((Cell) cell, true);
					if (CsjStrUtils.isNotEmpty(val)) {
						System.out.println("file path:["+path+"]sheet:["+sheet.getSheetName()+"]row:[" +  row.getRowNum() + "]col:[" + cell.getColumnIndex() + "]val:["
								+ getCellContent( cell, true) + "]");
					}

				}
			}

	}
	public static void printSheet(String filePath) throws Throwable {
			Workbook wb = getWorkBook(filePath);
			File f = new File(filePath);
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				printSheet(f.getAbsolutePath(),wb.getSheetAt(i));
			}

	}
	
	/**
	 * @param wb
	 * @param map
	 * @throws Throwable 
	 */
	public static void deleteSheetWithOutNms(Workbook wb, Set<String> sheetNmSet) throws Throwable {
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i);
				String snm = sheet.getSheetName();
				if (sheetNmSet.contains(snm)) {
					continue;
				}
				set.add(snm);
			}
			deleteSheetNm(wb, set);

	}
	
	/**
	 * @param wb
	 * @param map
	 * @throws Throwable 
	 */
	public static void deleteSheetWithOutNm(Workbook wb, String sheetNm) throws Throwable {
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i);
				String snm = sheet.getSheetName();
				if (sheetNm.equals(snm)) {
					continue;
				}
				set.add(snm);
			}
			deleteSheetNm(wb, set);

	}
	/**
	 * @param wb
	 * @param map
	 * @throws Throwable 
	 */
	public static void deleteSheetNm(Workbook wb, HashSet<String> set) throws Throwable {
			Iterator iterator = set.iterator();
			while(iterator.hasNext()){
				String sheetNm = iterator.next().toString();
				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
					Sheet sheet = wb.getSheetAt(i);
					String snm = sheet.getSheetName();
					if (snm.equals(sheetNm)) {
						wb.removeSheetAt(i);
						break;
					}
				}
			}


	}
	/**
	 * @param wb
	 * @param map
	 * @throws Throwable 
	 */
	public static void deleteWithOutSheetNm(Workbook wb, HashSet<String> set) throws Throwable {

			HashSet<String> deleteSheetNmSet = new HashSet<String>();
			for (int i = 0; i <wb.getNumberOfSheets(); i++) {
				String sheetNm = wb.getSheetAt(i).getSheetName();
				if (!set.contains(sheetNm)) {
					deleteSheetNmSet.add(sheetNm);
				}
			}
			deleteSheetNm(wb, deleteSheetNmSet);


	}
	/**
	 * @param cell
	 * @return
	 * @throws Throwable 
	 */
	public static String getCellComment(Cell cell) throws Throwable {
		
		String retStr = CsjConst.EMPTY;
			if (cell == null || cell.getCellComment() == null || cell.getCellComment().getString() == null) {

			} else {
				retStr = cell.getCellComment().getString().getString();
			}

		return retStr;
	}

	
	/**
	 * @param xlsPath
	 * @return
	 * @throws Throwable 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static List<String> getSheetNms(String xlsPath) throws Throwable {

		
		List<String> sheetList = new ArrayList<>();
		Workbook wb =getWorkBook(xlsPath) ;
			int sheetSum = wb.getNumberOfSheets();
			for (int i = 0; i < sheetSum; i++) {
				Sheet sheet = wb.getSheetAt(i);
				sheetList.add(sheet.getSheetName());
			}
			wb.close();

		return sheetList;
	}
	/**
	 * @param wb
	 * @param map
	 */
	public static void deleteSheetByNm(Workbook wb, HashSet<String> set, boolean isDelSheetNmInSet) {

		for (String sheetNm : set) {
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i);
				String snm = sheet.getSheetName();
				if (isDelSheetNmInSet) {
					if (snm.equals(sheetNm)) {
						wb.removeSheetAt(i);
						break;
					}
				} else {
					if (snm.equals(sheetNm) == false) {
						wb.removeSheetAt(i);
						break;
					}
				}

			}
		}
	}

	public static Workbook getWorkBook(String filePath) throws Throwable {
		File f = new File(filePath);
		FileInputStream fileIn = new FileInputStream(f);
		Workbook wb;
			if (CsjStrUtils.isEndByIgnor(f.getName(),CsjConst.EXCEL_DOT_XLS_1997)) {
				wb = new HSSFWorkbook(fileIn);
			} else {
				wb = new XSSFWorkbook(fileIn);
			}
		return wb;
	}
	public static void writeXls(Workbook wb,String xlsNmPath,String xlsNm) throws Throwable {
		FileOutputStream fileOut = null;
		File f = new File(xlsNmPath);
		f.mkdirs();
		String filePath = xlsNmPath+ xlsNm+ CsjConst.EXCEL_DOT_XLSX_2007;
		fileOut = new FileOutputStream(filePath);
		wb.write(fileOut);
		fileOut.close();
		}

	
	public static List<List<String>> getSchedualContents(String filePath,String sheetNm,boolean checkStrikeOut) throws Throwable {
		
		List<List<String>> retLst = new ArrayList<List<String>>();
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		Workbook wb = getWorkBook(filePath);
		Sheet sheet = wb.getSheet(sheetNm);
		
		for (Row row:sheet) {
			List<String> strLst = new ArrayList<String>();
			for (Cell cell : row) {
				strLst.add(getCellContent(cell, false));
			}
			retLst.add(strLst);
		}
		fileInputStream.close();
		return 	retLst;
	}
	public static void schedualXlsReplace(String filePath,String fileOutPath,String sheetNm,List<List<String>> strLst) throws Throwable{
			File file = new File(filePath);
			FileInputStream fileIn = new FileInputStream(file);
			
			Workbook wb = new XSSFWorkbook(fileIn);
			for (int i = wb.getNumberOfSheets()-1; i >=0 ; i--) {
				wb.removeSheetAt(i);
			}
			wb.createSheet(sheetNm);
			
			Sheet st = wb.getSheet(sheetNm);
			for (int i = 0; i < strLst.size(); i++) {
				List<String> lst = strLst.get(i);
				for (int j = 0; j < lst.size(); j++) {
					setCellValue(st, i, j, lst.get(j));
				}
			}
			
			FileOutputStream fileOut = new FileOutputStream(fileOutPath);
			wb.write(fileOut);
			fileOut.close();
			fileIn.close();
	}
	public static Map<Integer,String> getSheetContents(String filePath,String sheetNm,int col,int rowStart,int rowEnd) throws Throwable {
		Map<Integer,String> retMap = new HashMap<Integer, String>();
		FileInputStream fileInputStream = new FileInputStream(new File(
				filePath));
		Workbook wb = null;
		if (CsjStrUtils.isEndByIgnor(filePath, CsjConst.EXCEL_DOT_XLSX_2007)) {
			wb = new XSSFWorkbook(fileInputStream);
		} else if (CsjStrUtils.isEndByIgnor(filePath,CsjConst.EXCEL_DOT_XLS_1997)) {
			wb = new HSSFWorkbook(fileInputStream);
		}
		Sheet sheet = wb.getSheet(sheetNm);
		for (int i = rowStart; i <=rowEnd; i++) {
			Row row = sheet.getRow(i);
			retMap.put(i-rowStart, getCellContent(row, col, false));
		}
		fileInputStream.close();
		return 	retMap;
	}

	/**
	 * @param sheet
	 * @param cellInfo
	 * @throws Throwable
	 */
	public static void setCellValue(Sheet sheet, CsjCellInfo cellInfo)
			throws Throwable {
		if (cellInfo != null && CsjStrUtils.isNotEmpty(cellInfo.getContent()
				)) {
			setCellValue(sheet, cellInfo.getRowNum(), cellInfo.getCellNum(),
					cellInfo.getContent());
		}

	}

	/**
	 * @param string
	 * @param string2
	 * @param i
	 * @param j
	 * @param string3
	 * @throws Throwable
	 */
	public static void setCellValue(String path, String sheetNm, int row,
			int col, String str) throws Throwable {

		File file = new File(path);
		FileInputStream fileIn = new FileInputStream(file);
		
		Workbook wb = null;
		if (CsjStrUtils.isEndByIgnor(path, CsjConst.EXCEL_DOT_XLSX_2007)) {
			wb = new XSSFWorkbook(fileIn);
		} else {
			wb = new HSSFWorkbook(fileIn);
		}
		setCellValue(wb.getSheet(sheetNm),row,col,str);
		FileOutputStream fileOut = new FileOutputStream(path);
		wb.write(fileOut);
		fileOut.close();
		fileIn.close();

	}

	/**
	 * @param wb
	 * @param s_sheetNm
	 * @return
	 */
	public static Sheet getOneSheetByNm(Workbook wb, String sheetNm) {
		for (int i = wb.getNumberOfSheets()-1; i >=0 ; i--) {
			if (!wb.getSheetAt(i).getSheetName().equalsIgnoreCase(sheetNm)) {
				wb.removeSheetAt(i);
			}
		}
		return wb.getSheetAt(0);
	}

	/**
	 * @param newSt
	 * @param row
	 * @param b
	 * @throws Throwable 
	 */
	public static void copyRow(Sheet newSt, int rowIndex, Row row, boolean isCopyOthers) throws Throwable {
		if (newSt == null || row == null) {
			return;
		}
			for (Cell cell : row) {
				
				Cell newCell = setCellValueWithCs(newSt, rowIndex, cell.getColumnIndex(), getCellContent(cell, false), cell.getCellStyle());
				newCell.setCellType(cell.getCellType());
				newCell.setCellComment(cell.getCellComment());
				newCell.setCellFormula(cell.getCellFormula());
			}
	}
}
class CsjPatr {
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
