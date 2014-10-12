package core.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableFrame {
	public JFrame tableFrame;
	public JScrollPane scrollPane;
	public JTable table;
	public String[] columnNames;
	public Object[][] rowData;
	public DefaultTableModel model;

	public TableFrame() {
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
		{
			String tmp[] = { "Name", "Age", "Phone num" };
			columnNames = tmp;
		}
		rowData = new Object[2][3];
		rowData[0] = columnNames;
		model = new DefaultTableModel(0, 0);
		model.setColumnIdentifiers(columnNames);
		// table = new JTable(rowData, columnNames);
		table = new JTable();
		table.setModel(model);
		// tablePanel.add(table);
		// tablePanel.add( new JScrollPane(table));
		scrollPane = new JScrollPane(table);
		tableFrame.add(scrollPane, BorderLayout.CENTER);
		// scrollPane.add(table);
		// tablePanel.add(table);

		tableFrame.pack();
	}

	public void show() {
		tableFrame.setVisible(true);
	}

	public void hide() {
		tableFrame.setVisible(false);
	}

	public void end() {
		tableFrame.setVisible(false);
		tableFrame.dispose();
	}
}
