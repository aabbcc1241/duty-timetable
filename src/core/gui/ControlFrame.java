package core.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ControlFrame {
	public JFrame controlFrame;
	public JTextArea messageTextArea;
	private DutyTimeTable_GUI dutyTimeTable_GUI;

	public ControlFrame(DutyTimeTable_GUI dutyTimeTable_GUI) {
		this.dutyTimeTable_GUI = dutyTimeTable_GUI;
		init();
	}

	public void init() {
		/** master frame **/
		controlFrame = new JFrame();
		controlFrame.setBounds(100, 100, 900, 450);
		controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		Container controlPanel = new JPanel();
		controlFrame.getContentPane().add(controlPanel, BorderLayout.NORTH);

		Container messagePanel = new JPanel();
		controlFrame.getContentPane().add(messagePanel, BorderLayout.CENTER);

		messageTextArea = new JTextArea(10, 80);
		messageTextArea.setEditable(false);
		messagePanel.add(new JScrollPane(messageTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		JButton jButtonExit = new JButton("Exit");
		jButtonExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable_GUI.exit();
			}
		});
		controlPanel.add(jButtonExit);

		JButton jButtonGetFile = new JButton("Get File");
		jButtonGetFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable_GUI.dutyTimeTable.getFile();
			}
		});
		controlPanel.add(jButtonGetFile);

		JButton jButtonReadFile = new JButton("Read File");
		jButtonReadFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable_GUI.dutyTimeTable.readFile();
			}
		});
		controlPanel.add(jButtonReadFile);

		JButton jButtonGenerateCX = new JButton("Generate - cx");
		jButtonGenerateCX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable_GUI.dutyTimeTable.generate("cx");
			}
		});
		controlPanel.add(jButtonGenerateCX);

		JButton jButtonGenerateGrow = new JButton("Generate - grow");
		jButtonGenerateGrow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable_GUI.dutyTimeTable.generate("grow");
			}
		});
		controlPanel.add(jButtonGenerateGrow);

		JButton jButtonStop = new JButton("Stop (Generate)");
		jButtonStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable_GUI.dutyTimeTable.stop();
			}
		});
		controlPanel.add(jButtonStop);

		JButton jButtonSave = new JButton("Save");
		jButtonSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable_GUI.dutyTimeTable.save();
			}
		});
		controlPanel.add(jButtonSave);

		controlFrame.pack();
	}

	public void show() {
		controlFrame.setVisible(true);
	}

	public void hide() {
		controlFrame.setVisible(false);
	}

	public void end() {
		controlFrame.setVisible(false);
		controlFrame.dispose();
	}

}
