package core;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.LayoutManager;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Display extends OutputStream {
	public JFrame frame;
	public Container container;
	public JTextArea textArea;

	public long interval;
	public long lastUpdate;

	public Display(int interval) {
		this.interval = interval;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		textArea = new JTextArea(25, 80);
		textArea.setEditable(false);
		container.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		clear();
		update();
	}

	public Display() {
		this(0);
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		final String text = new String(buffer, offset, length);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textArea.append(text);
			}
		});
	}

	@Override
	public void write(int b) throws IOException {
		write(new byte[] { (byte) b }, 0, 1);
	}

	public void clear() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textArea.setText("");
			}
		});
	}

	public void update() {
		textArea.update(textArea.getGraphics());
		lastUpdate = System.currentTimeMillis();
	}

	public void checkUpdate() {
		if (System.currentTimeMillis() - lastUpdate >= interval)
			update();
	};
}
