package test;

import core.DutyTimeTable;

public class DutyTimeTableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String url =
		// "https://docs.google.com/spreadsheet/pub?key=0AjBJCFRK44scdDcwd21mSXdWaU43Y19JUEtoVDBJWnc&output=xls";
		String url = "https://docs.google.com/spreadsheet/pub?key=0AjBJCFRK44scdG9uamRqajhvTE1IZUtwZHd3QjFZNWc&output=xls";
		String path = "FromGoogle";
		String inFilename = "MIC.xls";
		String outFilename = "MIC-old.xls";
		String saveFilename="MIC-save.xls";
		int weekNum = 4;
		DutyTimeTable dutyTimeTable = new DutyTimeTable(url, path, inFilename, outFilename, saveFilename,weekNum);
		dutyTimeTable.menu();
		dutyTimeTable.end();
	}

}
