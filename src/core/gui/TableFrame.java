package core.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import core.dutytable.MIC;

public class TableFrame {
	public JFrame tableFrame;
	public JScrollPane scrollPane;
	public JTable table;
	public String[] columnNames;
	public DefaultTableModel model;

	private DutyTimeTable_GUI dutyTimeTable_GUI;

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
		model = new DefaultTableModel(0, 0);
		model.setColumnIdentifiers(columnNames);
		table = new JTable();
		table.setModel(model);
		scrollPane = new JScrollPane(table);
		// tableFrame.add(scrollPane, BorderLayout.CENTER);
		tableFrame.add(table, BorderLayout.CENTER);
		// show();
	}

	public void show() {
		tableFrame.pack();
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
		while (model.getRowCount() > 0)
			model.removeRow(0);
		Object[] rowData = new Object[mic.days.length];
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
			for (int iDay = 0; iDay < mic.days.length; iDay++)
				rowData[iDay] = 0;
			model.addRow(rowData);
		}
		tableFrame.pack();
	}

	public void update(MIC mic) {
		while (model.getRowCount() > 0)
			model.removeRow(0);
		Object[] rowData = new Object[mic.days.length];		
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
			for (int iDay = 0; iDay < mic.days.length; iDay++)
				rowData[iDay] = mic.days[iDay].timeslot[iTimeslot].worker.name;
			model.addRow(rowData);
		}
		tableFrame.pack();
	}
}
