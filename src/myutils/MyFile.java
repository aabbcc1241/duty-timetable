package myutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;

public abstract class MyFile {
	public static void createDir(String path) {
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
	}

	public static void getFile(String url, String path, String inFilename,
			String outFilename) throws IOException {
		URL website = new URL(url);
		ReadableByteChannel readableByteChannel = Channels.newChannel(website
				.openStream());
		FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + inFilename);
		fileOutputStream.getChannel()
				.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		fileOutputStream.close();
		System.out.println("Downloaded to <" + inFilename + ">");
		System.out.println("(" + new File(path + "/" + inFilename).getAbsolutePath()
				+ ")");
		System.out.println("Please save as <" + outFilename
				+ "> ( Microsoft Excel 97/2000/XP/2003 <.xls> )");
	}

	public static void deleteFile(String pathname) {
		File file = new File(pathname);
		if (file.exists())
			file.delete();
	}

	public static Workbook getWorkbook(String path, String outFilename)
			throws BiffException, IOException {
		return Workbook.getWorkbook(new File(path + "/" + outFilename));
	}

	public static WritableWorkbook getWritableWorkbook(String path, String outFilename)
			throws BiffException, IOException {
		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setLocale(new Locale("en", "EN"));
		return Workbook.createWorkbook(new File(path + "/" + outFilename),
				workbookSettings);
	}
}
