import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import myutils.Display;
import core.DutyTimeTable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.OutputStream;
import java.io.PrintStream;

public class DutyTimeTable_GUI {

	private PrintStream DEFAULT_SYSTEM_PRINTSCREAM;

	/* GUI stuff */

	private JFrame masterFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DutyTimeTable_GUI window = new DutyTimeTable_GUI();
					window.masterFrame.setVisible(true);
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
		masterFrame = new JFrame();
		masterFrame.setBounds(100, 100, 900, 450);
		masterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		masterFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		Container controlPanel = new JPanel();
		masterFrame.getContentPane().add(controlPanel, BorderLayout.NORTH);

		Container messagePanel = new JPanel();
		masterFrame.getContentPane().add(messagePanel, BorderLayout.CENTER);

		JTextArea messageTextArea = new JTextArea(25, 80);
		messageTextArea.setEditable(false);
		messagePanel.add(new JScrollPane(messageTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		JButton jButtonExit = new JButton("Exit");
		jButtonExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				exit();
			}
		});
		controlPanel.add(jButtonExit);

		JButton jButtonGetFile = new JButton("Get File");
		jButtonGetFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable.getFile();
			}
		});
		controlPanel.add(jButtonGetFile);

		JButton jButtonReadFile = new JButton("Read File");
		jButtonReadFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable.readFile();
			}
		});
		controlPanel.add(jButtonReadFile);

		JButton jButtonGenerateCX = new JButton("Generate - cx");
		jButtonGenerateCX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable.generate("cx");
			}
		});
		controlPanel.add(jButtonGenerateCX);

		JButton jButtonGenerateGrow = new JButton("Generate - grow");
		jButtonGenerateGrow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable.generate("grow");
			}
		});
		controlPanel.add(jButtonGenerateGrow);

		JButton jButtonSave = new JButton("Save");
		jButtonSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable.save();
			}
		});
		controlPanel.add(jButtonSave);

		Display display = new Display(messageTextArea);
		setSystemOut(display);

		dutyTimeTable = new DutyTimeTable(display);
	}

	private void restoreSystemOut() {
		System.setOut(DEFAULT_SYSTEM_PRINTSCREAM);
	}

	private void setSystemOut(OutputStream outputStream) {
		System.setOut(new PrintStream(outputStream));
	}

	private void setSystemOutToJTextArea() {
		setSystemOut(dutyTimeTable.display);
	}

	/* core stuff (algorithm on DutyTimeTable) */
	private DutyTimeTable dutyTimeTable;

	private void exit() {
		restoreSystemOut();
		masterFrame.setVisible(false);
		masterFrame.dispose();
		Runtime.getRuntime().exit(0);
	}
}
