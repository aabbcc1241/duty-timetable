package core;

import java.util.Scanner;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utils.Utils;

public class DutyTimeTable {
	private String path;
	private String url;
	private String inFilename;
	private String outFilename;
	int weekNum;
	MIC mic;
	Worker[] workers;

	public DutyTimeTable(String url, String path, String inFilename, String outFilename, int weekNum) {
		this.url = url;
		this.path = path;
		this.inFilename = inFilename;
		this.outFilename = outFilename;
		this.weekNum = weekNum;
		mic = new MIC();
		workers = new Worker[10];
		for (int i = 0; i < workers.length; i++)
			workers[i] = new Worker();
	}

	public void menu() {
		Scanner scanner = new Scanner(System.in);
		int option = -1;
		do {
			System.out.println();
			System.out.println("0. exit");
			System.out.println("1. getFile");
			System.out.println("2. readFile");
			try {
				option = scanner.nextInt();
			} catch (Exception e) {
			}
			switch (option) {
			case 0:
				System.out.println("leaving");
				break;
			case 1:
				Utils.createDir(path);
				try {
					Utils.getFile(url, path, inFilename, outFilename);
				} catch (IOException e) {
					System.out.println("cannot getFile");
				}
				break;
			case 2:
				readFile();
				break;
			default:
				System.out.println("error input");
				break;
			}
		} while (option != 0);
		scanner.close();
	}

	private void readFile() {
		try {
			Workbook workbook;
			workbook = Utils.getWorkbook(path, outFilename);
			Sheet sheet;
			Cell cell;
			String str;
			int dayAmount = workers[0].days.length;
			int workerAmount = workers.length;
			int timeslotAmount = workers[0].days[0].timeslot.length;
			for (int sheetIndex = 1; sheetIndex <= dayAmount; sheetIndex++) {
				sheet = workbook.getSheet(sheetIndex);
				for (int rowIndex = 1; rowIndex <= workerAmount; rowIndex++) {
					for (int colIndex = 1; colIndex <= timeslotAmount; colIndex++) {
						cell = sheet.getCell(colIndex, rowIndex);
						str = cell.getContents();
						System.out.println();
						System.out.println(sheetIndex + "\t" + rowIndex + "\t" + colIndex);
						workers[rowIndex - 1].days[sheetIndex - 1].timeslot[colIndex - 1].status = (str
								.length() != 0) ? Integer.parseInt(str) : 0;
					}
				}
			}
		} catch (BiffException | IOException e) {
			System.out.println("cannot readFile");
		}
	}
}
