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

import core.DutyTimeTable;

public class DutyTimeTable_GUI {

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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		masterFrame = new JFrame();
		masterFrame.setBounds(100, 100, 800, 600);
		masterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		masterFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		Container controlPanel = new JPanel();
		// controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		masterFrame.getContentPane().add(controlPanel, BorderLayout.NORTH);

		Container messagePanel = new JPanel();
		masterFrame.getContentPane().add(messagePanel, BorderLayout.CENTER);

		JTextArea textArea = new JTextArea(25, 80);
		textArea.setEditable(false);
		messagePanel.add(new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		JButton jButtonExit = new JButton("Exit");
		controlPanel.add(jButtonExit);

		JButton jButtonGetFile = new JButton("Get File");
		controlPanel.add(jButtonGetFile);

		JButton jButtonReadFile = new JButton("Read File");
		controlPanel.add(jButtonReadFile);

		JButton jButtonGenerateCX = new JButton("Generate - cx");
		controlPanel.add(jButtonGenerateCX);

		JButton jButtonGenerateGrow = new JButton("Generate - grow");
		controlPanel.add(jButtonGenerateGrow);

		JButton jButtonSave = new JButton("Save");
		controlPanel.add(jButtonSave);
		
		dutyTimeTable=new DutyTimeTable();
	}
	
	/* core stuff (algorithm on DutyTimeTable)*/
	private DutyTimeTable dutyTimeTable;
	
	
}
