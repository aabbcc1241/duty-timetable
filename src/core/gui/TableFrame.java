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

import core.dutytable.mic.MIC;

public class TableFrame implements Runnable {
	public JFrame tableFrame;
	public JScrollPane infoScrollPane;
	public JScrollPane mainScrollPane;
	public JTable infoTable;
	public JTable mainTable;
	public String[] infoColumnNames;
	public String[] mainColumnNames;
	public DefaultTableModel infoModel;
	public DefaultTableModel mainModel;

	private DutyTimeTable_GUI dutyTimeTable_GUI;
	public InfoCarrier infoCarrier;

	private Thread thread;
	private boolean shouldUpdate = false;

	public TableFrame(DutyTimeTable_GUI dutyTimeTable_GUI) {
		this.dutyTimeTable_GUI = dutyTimeTable_GUI;
		init();
	}

	public void init() {
		/** frame **/
		tableFrame = new JFrame();
		tableFrame.setBounds(100, 100, 800, 600);
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
		/* infotable */
		{
			String tmp[] = { "name", "value" };
			infoColumnNames = tmp;
		}
		infoModel = new DefaultTableModel(0, 0);
		infoModel.setColumnIdentifiers(infoColumnNames);
		infoTable = new JTable();
		infoTable.setModel(infoModel);
		tableFrame.add(infoTable, BorderLayout.CENTER);

		/* main table */
		{
			String tmp[] = { "星期一", "星期二", "星期三", "星期四", "星期五" };
			mainColumnNames = tmp;
		}
		mainModel = new DefaultTableModel(0, 0);
		mainModel.setColumnIdentifiers(mainColumnNames);
		mainTable = new JTable();
		mainTable.setModel(mainModel);
		mainScrollPane = new JScrollPane(mainTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainScrollPane.setBorder(null);
		tableFrame.add(mainScrollPane, BorderLayout.SOUTH);
		// tableFrame.add(mainTable, BorderLayout.CENTER);
		// show();
	}

	public void pack() {
		infoTable.setSize(infoTable.getPreferredSize());
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
		if (thread == null)
			thread = new Thread(this);
		thread.start();
	}

	public void stopUpdate() {
		shouldUpdate = false;
	}

	public void update() {
		if (infoCarrier == null)
			return;
		/** info table **/
		while (infoModel.getRowCount() > 0)
			infoModel.removeRow(0);
		Object[] infoRowData = new Object[2];
		infoRowData[0] = "Population";
		infoRowData[1] = infoCarrier.popSize;
		infoModel.addRow(infoRowData);
		infoRowData[0] = "Generation";
		infoRowData[1] = infoCarrier.iGen;
		infoModel.addRow(infoRowData);
		infoRowData[0] = "Avg Fitness";
		infoRowData[1] = infoCarrier.avgFitness;
		infoModel.addRow(infoRowData);
		infoRowData[0] = "SD Fitness";
		infoRowData[1] = infoCarrier.sdFitness;
		infoModel.addRow(infoRowData);
		infoRowData[0] = "Best Fitness";
		infoRowData[1] = infoCarrier.bestLife.fitness;
		infoModel.addRow(infoRowData);
		infoRowData[0] = "Hour SD";
		infoRowData[1] = infoCarrier.bestLife.hoursSd;
		infoModel.addRow(infoRowData);
		/** main talbe **/
		MIC mic = infoCarrier.mic;
		Object[] mainRowData = new Object[mic.days.length];
		while (mainModel.getRowCount() > 0)
			mainModel.removeRow(0);
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
			for (int iDay = 0; iDay < mic.days.length; iDay++)
				mainRowData[iDay] = mic.days[iDay].timeslot[iTimeslot].worker.name;
			mainModel.addRow(mainRowData);
		}
		pack();
	}

	@Override
	public void run() {
		while (shouldUpdate) {
			try {
				Thread.sleep(1000);
				update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
