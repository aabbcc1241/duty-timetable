package test;

import java.io.IOException;

import core.DutyTimeTable;

public class DutyTimeTableTest {

	/**
	 * @param args
	 */
	public static void mainOld(String[] args) {
		String url = "https://docs.google.com/spreadsheet/pub?key=0AjBJCFRK44scdG9uamRqajhvTE1IZUtwZHd3QjFZNWc&output=xls";
		String path = "FromGoogle";
		String inFilename = "MIC.xls";
		String outFilename = "MIC-old.xls";
		String saveFilename = "MIC-save.xls";
		int weekNum = 4;
		DutyTimeTable dutyTimeTable = new DutyTimeTable(url, path, inFilename,
				outFilename, saveFilename, weekNum);
		try {
			dutyTimeTable.loadSettings();
		} catch (IOException e) {
			System.out.println("failed to find properties file");
			e.printStackTrace();
		}
		dutyTimeTable.menu();
		//dutyTimeTable.end();
	}

	public static void main(String[] args) {		
		DutyTimeTable dutyTimeTable = new DutyTimeTable();
		dutyTimeTable.menu();		
	}

}
