package core;

import java.util.Properties;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JTextArea;

import myutils.Display;
import myutils.MyFile;
import myutils.StringUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class DutyTimeTable {
	public static final String PROPERTIES_FILE = "dutytimetable.properties";
	public static final int WORKER_AMOUNT = 10;
	private String path;
	private String url;
	private String inFilename;
	private String outFilename;
	private String saveFilename;
	int weekNum;
	MIC mic;
	Worker[] workers;
	MIC_GA mic_GA;

	public Display display;
	private JTextArea messageTextArea;

	public DutyTimeTable(String url, String path, String inFilename, String outFilename, String saveFilename,
			int weekNum) {
		this.url = url;
		this.path = path;
		this.inFilename = inFilename;
		this.outFilename = outFilename;
		this.saveFilename = saveFilename;
		this.weekNum = weekNum;
		init();
	}

	public DutyTimeTable(JTextArea messageTextArea) {
		this.messageTextArea = messageTextArea;
		this.display=new Display(messageTextArea);
		try {
			loadSettings();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("failed to find properties file");
			System.out.println("loading default values");
			loadDefaultSettings();
		}
		init_skipDisplay();
	}

	public DutyTimeTable(Display display) {
		this.display = display;
		try {
			loadSettings();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("failed to find properties file");
			System.out.println("loading default values");
			loadDefaultSettings();
		}
		init_skipDisplay();
	}

	public void loadDefaultSettings() {
		url = "https://docs.google.com/spreadsheet/pub?key=0AjBJCFRK44scdG9uamRqajhvTE1IZUtwZHd3QjFZNWc&output=xls";
		path = "MIC_EXCEL";
		inFilename = "MIC.xls";
		outFilename = "MIC-old.xls";
		saveFilename = "MIC-save.xls";
		weekNum = 4;
	}

	public void loadSettings(String url, String path, String inFilename, String outFilename,
			String saveFilename, int weekNum) {
		this.url = url;
		this.path = path;
		this.inFilename = inFilename;
		this.outFilename = outFilename;
		this.saveFilename = saveFilename;
		this.weekNum = weekNum;

	}

	public void loadSettings() throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = new FileInputStream(PROPERTIES_FILE);
		properties.load(inputStream);
		url = properties.getProperty("url").trim();
		path = properties.getProperty("path").trim();
		inFilename = properties.getProperty("inFilename").trim();
		outFilename = properties.getProperty("outFilename").trim();
		saveFilename = properties.getProperty("saveFilename").trim();
		weekNum = Integer.parseInt(properties.getProperty("weekNum").trim());
	}

	public void init() {
		mic = new MIC();
		workers = new Worker[WORKER_AMOUNT];
		for (int iWorker = 0; iWorker < workers.length; iWorker++)
			workers[iWorker] = new Worker(iWorker);
		display = new Display(messageTextArea);
		display.setFPS(2);
	}

	public void init_skipDisplay() {
		mic = new MIC();
		workers = new Worker[WORKER_AMOUNT];
		for (int iWorker = 0; iWorker < workers.length; iWorker++)
			workers[iWorker] = new Worker(iWorker);
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
			System.out.println("5. save");
			try {
				option = scanner.nextInt();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			switch (option) {
			case 0:
				System.out.println("leaving");
				display.end();
				break;
			case 1:
				getFile();
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
			case 5:
				save();
				break;
			default:
				System.out.println("wrong input!");
				break;
			}
		} while (option != 0);
		scanner.close();
	}

	public void getFile() {
		MyFile.createDir(path);
		try {
			MyFile.getFile(url, path, inFilename, outFilename);
		} catch (IOException e) {
			System.out.println("cannot getFile");
		}
	}

	public void readFile() {
		try {
			// display.show();
			Workbook workbook;
			// System.out.println("getting from<" + path + "\\" + outFilename +
			// ">");
			display.write("getting from<" + path + "\\" + outFilename + ">");
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
								.length() != 0) ? Integer.parseInt(str) : -1;
						if (workers[rowIndex - 1].days[sheetIndex - 1].timeslot[colIndex - 1].status >= 10)
							workers[rowIndex - 1].days[sheetIndex - 1].timeslot[colIndex - 1].status /= 10;
					}
				}
			}
			showWorkerInfo();
		} catch (BiffException | IOException e) {
			// System.out.println("cannot readFile");
			display.write("cannot readFile");
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

	public void generate(String mode) {
		mic.findPossibleWorkers(workers);
		mic_GA = new MIC_GA(mic, workers, display);
		mic_GA.start(mode);
	}

	public void stop() {
		mic_GA.stop();
	}

	public void save() {
		saveMicToWorker();
		saveToFile();
	}

	private void saveMicToWorker() {
		Worker worker;
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
			for (int iDay = 0; iDay < mic.days.length; iDay++) {
				worker = mic.days[iDay].timeslot[iTimeslot].worker;
				if (worker != null)
					switch (worker.days[iDay].timeslot[iTimeslot].status) {
					case 1:
						worker.days[iDay].timeslot[iTimeslot].status = 10;
						break;
					case 2:
						worker.days[iDay].timeslot[iTimeslot].status = 20;
						break;
					}
			}
		}
	}

	private void saveToFile() {
		try {
			WritableWorkbook workbook;
			System.out.println("saving to<" + path + "\\" + saveFilename + ">");
			MyFile.deleteFile(path + "\\" + saveFilename);
			workbook = MyFile.getWritableWorkbook(path, saveFilename);
			WritableSheet sheet;
			jxl.write.Number number;
			double val;
			int dayAmount = workers[0].days.length;
			int workerAmount = workers.length;
			int timeslotAmount = workers[0].days[0].timeslot.length;
			/** MIC **/
			workbook.createSheet("MIC", 0);
			sheet = workbook.getSheet(0);
			Label label = new Label(0, 0, "week " + weekNum);
			sheet.addCell(label);
			String name;
			for (int iDay = 0; iDay < mic.days.length; iDay++)
				for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
					if (mic.days[iDay].timeslot[iTimeslot].worker == null)
						name = "";
					else
						name = mic.days[iDay].timeslot[iTimeslot].worker.name;
					label = new Label(iDay + 1, iTimeslot + 1, name);
					sheet.addCell(label);
				}
			/** Days Timeslots status **/
			for (int sheetIndex = 1; sheetIndex <= dayAmount; sheetIndex++) {
				workbook.createSheet("day-" + sheetIndex, sheetIndex);
				sheet = workbook.getSheet(sheetIndex);
				for (int rowIndex = 1; rowIndex <= workerAmount; rowIndex++) {
					for (int colIndex = 1; colIndex <= timeslotAmount; colIndex++) {
						val = workers[rowIndex - 1].days[sheetIndex - 1].timeslot[colIndex - 1].status;
						if (val == -1)
							continue;
						number = new jxl.write.Number(colIndex, rowIndex, val);
						sheet.addCell(number);
					}
				}
			}
			workbook.write();
			workbook.close();
		} catch (BiffException | IOException e) {
			System.out.println("cannot writeFile");
			e.printStackTrace();
		} catch (RowsExceededException e) {
			System.out.println("cannot writeFile");
			e.printStackTrace();
		} catch (WriteException e) {
			System.out.println("cannot writeFile");
			e.printStackTrace();
		}
	}
}
