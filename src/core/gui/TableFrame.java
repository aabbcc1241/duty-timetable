package core.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import core.dutytable.MIC;

public class TableFrame implements Runnable{
	public JFrame tableFrame;
	public JScrollPane scrollPane;
	public JTable mainTable;
	public String[] columnNames;
	public DefaultTableModel mainModel;

	private DutyTimeTable_GUI dutyTimeTable_GUI;

	private Thread thread;
	private boolean shouldUpdate = false;

	public TableFrame(DutyTimeTable_GUI dutyTimeTable_GUI) {
		this.dutyTimeTable_GUI = dutyTimeTable_GUI;
		init();
	}

	public void init() {
		/** frame **/
		tableFrame = new JFrame();
		tableFrame.setBounds(10, 10, 800, 600);
		tableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tableFrame.setResizable(true);
		tableFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		/** content **/
		/* button */
		JButton jButtonStop = new JButton("Stop (Generate)");
		jButtonStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dutyTimeTable_GUI.dutyTimeTable.stop();
			}
		});
		tableFrame.add(jButtonStop, BorderLayout.NORTH);
		/* table */
		{
			String tmp[] = { "星期一", "星期二", "星期三", "星期四", "星期五" };
			columnNames = tmp;
		}
		mainModel = new DefaultTableModel(0, 0);
		mainModel.setColumnIdentifiers(columnNames);
		mainTable = new JTable();
		mainTable.setModel(mainModel);
		scrollPane = new JScrollPane(mainTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		tableFrame.add(scrollPane, BorderLayout.CENTER);
		// tableFrame.add(mainTable, BorderLayout.CENTER);
		// show();
	}

	public void pack() {
		mainTable.setPreferredScrollableViewportSize(mainTable.getPreferredSize());
		tableFrame.pack();
	}

	public void show() {
		pack();
		tableFrame.setVisible(true);
	}

	public void hide() {
		tableFrame.setVisible(false);
	}

	public void end() {
		tableFrame.setVisible(false);
		tableFrame.dispose();
	}

	public void reInit(MIC mic) {
		while (mainModel.getRowCount() > 0)
			mainModel.removeRow(0);
		Object[] rowData = new Object[mic.days.length];
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
			for (int iDay = 0; iDay < mic.days.length; iDay++)
				rowData[iDay] = 0;
			mainModel.addRow(rowData);
		}
		pack();
	}

	public void startUpdate() {
		shouldUpdate = true;
		if(thread==null)
			thread=new Thread(this);
		thread.start();
	}

	public void stopUpdate() {
		shouldUpdate = false;
	}

	public void update() {
		while (mainModel.getRowCount() > 0)
			mainModel.removeRow(0);
		MIC mic=dutyTimeTable_GUI.dutyTimeTable.mic;
		Object[] rowData = new Object[mic.days.length];
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
			for (int iDay = 0; iDay < mic.days.length; iDay++)
				rowData[iDay] = mic.days[iDay].timeslot[iTimeslot].worker.name;
			mainModel.addRow(rowData);
		}
		pack();
	}

	@Override
	public void run() {
		while(shouldUpdate){			
			try {
				Thread.sleep(1000);
				update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
