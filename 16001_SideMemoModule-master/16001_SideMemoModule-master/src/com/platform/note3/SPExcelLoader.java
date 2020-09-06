package com.platform.note3;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SPExcelLoader {
	// http://jsonobject.tistory.com/127

	XSSFSheet sheet;

	public String strValue(Cell cell) {
		String value = null;
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue() + "";
		}
		return value;
	}

	public String strValueAt(int rowIndex, int colIndex) {
		Row row = sheet.getRow(rowIndex);
		Cell cell = row.getCell(colIndex);
		return strValue(cell);
	}

	public List<OV_Task> test99() {
		LinkedList<OV_Task> list = new LinkedList<OV_Task>();
		int count = 1;
		String value = new String();
		try {
			FileInputStream fis = new FileInputStream("C:\\Users\\EJAEJEO\\Desktop\\개인폴더\\TODO.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet("todo");
			for (int row = 0; row < 100; row++) {
				OV_Task task = new OV_Task();
				String v = strValueAt(row, 3);
				if (v.indexOf("!END") >= 0) {
					System.out.println("break- -----------------");
					break;
				}
				task.priority = strValueAt(row, 2);
				if (task.priority != null && task.priority.indexOf("완료") >= 0) {
					System.out.println("(완료)" + v);
					continue;
				}
				task.category = strValueAt(row, 1);
				task.subject = new String(v);

				list.add(task);
			}
			// Row row = sheet.getRow(0);
			// Cell cell = row.getCell(0);
			// System.out.println("value "+strValue(cell));
			// for (Row row : sheet) {
			// for (Cell cell : row) {
			// switch (cell.getCellType()) { // 각 셀에 담겨있는 데이터의 타입을 체크하고 해당 타입에 맞게 가져온다.
			// case HSSFCell.CELL_TYPE_NUMERIC:
			// value = cell.getNumericCellValue() + "";
			// break;
			// case HSSFCell.CELL_TYPE_STRING:
			// value = cell.getStringCellValue() + "";
			// break;
			// case HSSFCell.CELL_TYPE_BLANK:
			// value = cell.getBooleanCellValue() + "";
			// break;
			// case HSSFCell.CELL_TYPE_ERROR:
			// value = cell.getErrorCellValue() + "";
			// break;
			// }
			// System.out.println(value);
			// }
			// }
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	public void test() {

		try {
			FileInputStream fis = new FileInputStream("C:\\Users\\EJAEJEO\\Desktop\\개인폴더\\TODO.xlsx");
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0); // 해당 엑셀파일의 시트(Sheet) 수
			int rows = sheet.getPhysicalNumberOfRows(); // 해당 시트의 행의 개수
			for (int rowIndex = 1; rowIndex < rows; rowIndex++) {
				HSSFRow row = sheet.getRow(rowIndex); // 각 행을 읽어온다
				if (row != null) {
					int cells = row.getPhysicalNumberOfCells();
					for (int columnIndex = 0; columnIndex <= cells; columnIndex++) {
						HSSFCell cell = row.getCell(columnIndex); // 셀에 담겨있는 값을 읽는다.
						String value = "";
						switch (cell.getCellType()) { // 각 셀에 담겨있는 데이터의 타입을 체크하고 해당 타입에 맞게 가져온다.
						case HSSFCell.CELL_TYPE_NUMERIC:
							value = cell.getNumericCellValue() + "";
							break;
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue() + "";
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							value = cell.getBooleanCellValue() + "";
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = cell.getErrorCellValue() + "";
							break;
						}
						System.out.println(value);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
}
