package core;

import java.util.Scanner;
import java.io.IOException;

import myutils.Display;
import myutils.MyFile;
import myutils.StringUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DutyTimeTable {
	public static final int WORKER_AMOUNT = 10;
	private String path;
	private String url;
	private String inFilename;
	private String outFilename;
	int weekNum;
	MIC mic;
	Worker[] workers;

	Display display;

	public DutyTimeTable(String url, String path, String inFilename, String outFilename,
			int weekNum) {
		this.url = url;
		this.path = path;
		this.inFilename = inFilename;
		this.outFilename = outFilename;
		this.weekNum = weekNum;
		mic = new MIC();
		workers = new Worker[WORKER_AMOUNT];
		for (int iWorker = 0; iWorker < workers.length; iWorker++)
			workers[iWorker] = new Worker(iWorker);
		display = new Display();
		display.setFPS(2);
	}

	public void menu() {
		Scanner scanner = new Scanner(System.in);
		int option = -1;
		do {
			System.out.println();
			System.out.println("0. exit");
			System.out.println("1. getFile");
			System.out.println("2. readFile");
			System.out.println("3. generate-cx");
			System.out.println("4. generate-grow");
			try {
				option = scanner.nextInt();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			switch (option) {
			case 0:
				System.out.println("leaving");
				break;
			case 1:
				MyFile.createDir(path);
				try {
					MyFile.getFile(url, path, inFilename, outFilename);
				} catch (IOException e) {
					System.out.println("cannot getFile");
				}
				break;
			case 2:
				readFile();
				break;
			case 3:
				generate("cx");
				break;
			case 4:
				generate("grow");
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
			System.out.println("geting from<" + path + "\\" + outFilename + ">");
			workbook = MyFile.getWorkbook(path, outFilename);
			Sheet sheet;
			Cell cell;
			String str;
			int dayAmount = workers[0].days.length;
			int workerAmount = workers.length;
			int timeslotAmount = workers[0].days[0].timeslot.length;
			{
				sheet = workbook.getSheet(0);
				for (int rowIndex = 3; rowIndex < 3 + workerAmount; rowIndex++) {
					cell = sheet.getCell(7, rowIndex);
					workers[rowIndex - 3].name = cell.getContents().trim();
				}
			}
			for (int sheetIndex = 1; sheetIndex <= dayAmount; sheetIndex++) {
				sheet = workbook.getSheet(sheetIndex);
				for (int rowIndex = 1; rowIndex <= workerAmount; rowIndex++) {
					for (int colIndex = 1; colIndex <= timeslotAmount; colIndex++) {
						cell = sheet.getCell(colIndex, rowIndex);
						str = cell.getContents();
						workers[rowIndex - 1].days[sheetIndex - 1].timeslot[colIndex - 1].status = (str
								.length() != 0) ? Integer.parseInt(str) : 0;
					}
				}
			}
			showWorkerInfo();
		} catch (BiffException | IOException e) {
			System.out.println("cannot readFile");
		}
	}

	private void showWorkerInfo() {
		/** show worker info **/
		display.clearBuffer();
		for (int iWorker = 0; iWorker < workers.length; iWorker++) {
			display.writeBuffer("\n" + workers[iWorker].name);
			for (int iDay = 0; iDay < workers[iWorker].days.length; iDay++) {
				display.writeBuffer("\n\tDay-" + iDay + "\t");
				for (int iTimeslot = 0; iTimeslot < workers[iWorker].days[iDay].timeslot.length; iTimeslot++)
					display.writeBuffer(StringUtils.center(""
							+ workers[iWorker].days[iDay].timeslot[iTimeslot].status, 4));
			}
		}
		display.updateBuffer();
	}

	private void generate(String mode) {
		mic.findPossibleWorkers(workers);
		MIC_GA mic_GA = new MIC_GA(mic, workers, display);
		mic_GA.start(mode);
	}
}
