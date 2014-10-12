package core.gui;

import java.awt.EventQueue;

import myutils.Display;
import java.io.OutputStream;
import java.io.PrintStream;

import core.dutytable.DutyTimeTable;

public class DutyTimeTable_GUI {

	private PrintStream DEFAULT_SYSTEM_PRINTSCREAM;

	/* GUI stuff */
	private ControlFrame controlFrame;
	private TableFrame tableFrame;
	private Display display;

	/* core stuff (algorithm on DutyTimeTable) */
	public DutyTimeTable dutyTimeTable;

	/**
	 * Launch the application.
	 */
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DutyTimeTable_GUI window = new DutyTimeTable_GUI();
					window.controlFrame.show();
					// window.tableFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DutyTimeTable_GUI() {
		DEFAULT_SYSTEM_PRINTSCREAM = System.out;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param a
	 */
	private void initialize() {
		/* user interface */
		controlFrame = new ControlFrame(this);
		tableFrame = new TableFrame();
		display = new Display(controlFrame.messageTextArea);
		setSystemOut(display);
		/* core part */
		dutyTimeTable = new DutyTimeTable(display);
	}

	private void restoreSystemOut() {
		System.setOut(DEFAULT_SYSTEM_PRINTSCREAM);
	}

	private void setSystemOut(OutputStream outputStream) {
		System.setOut(new PrintStream(outputStream));
	}

	public void exit() {
		restoreSystemOut();
		controlFrame.end();
		tableFrame.end();
		Runtime.getRuntime().exit(0);
	}
}
