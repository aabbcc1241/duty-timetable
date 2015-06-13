package dutytimetable.gui;

import dutytimetable.core.EventHandler;
import dutytimetable.debug.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hkpu on 15/5/2015.
 */
public class CoreJFrame extends JFrame {
    private static CoreJFrame instance = null;

    public static CoreJFrame getInstance() {
        if (instance == null)
            synchronized (CoreJFrame.class) {
                if (instance == null)
                    instance = new CoreJFrame();
            }
        return instance;
    }

    JPanel contentPanel;
    private JToolBar mainToolBar;
    public JPanel mainPanel;
    private JPanel progressPanel;
    private JProgressBar progressBar;
    private JLabel progressLabel;
    private JButton buttonGenerate;
    private JButton buttonReset;
    private JButton buttonOpen;
    private JButton buttonSave;
    private JButton buttonExit;
    private final String GENERATE_START = "Generate";
    private final String GENERATE_START_TOOLTIPS = "Start to generate solution";
    private final String GENERATE_PAUSE = "Pause";
    private final String GENERATE_PAUSE_TOOLTIPS = "Pause generating solution";
    private final String GENERATE_RESUME = "Resume";
    private final String GENERATE_RESUME_TOOLTIPS = "Resume to generate solution";

    public CoreJFrame() {
        instance = this;

        setMinimumSize(new Dimension(600, 400));

        buttonOpen = new JButton("Open");
        buttonOpen.setToolTipText("Load from excel file");
        buttonOpen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openAction();
            }
        });
        buttonSave = new JButton("Save");
        buttonSave.setToolTipText("Save to excel file");
        buttonSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveAction();
            }
        });
        buttonGenerate = new JButton(GENERATE_START);
        buttonGenerate.setToolTipText(GENERATE_START_TOOLTIPS);
        buttonGenerate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeStatus(buttonGenerate);
            }
        });
        buttonReset = new JButton("Reset");
        buttonReset.setToolTipText("Reset the solution");
        buttonReset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventHandler.resetGenerate();
            }
        });
        buttonExit = new JButton("Exit");
        buttonExit.setToolTipText("Leave the program");
        buttonExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onLeave();
            }
        });

        //main toolbar
        mainToolBar.add(buttonOpen);
        mainToolBar.add(buttonSave);
        mainToolBar.addSeparator(new Dimension(50, 1));
        mainToolBar.add(buttonGenerate);
        mainToolBar.add(buttonReset);
        mainToolBar.addSeparator(new Dimension(50, 1));
        mainToolBar.add(buttonExit);

        //main panel bar
        //mainPanel.setUI(SpreadSheetViewer.getViewerPanel(Facilitator.path()).getUI());
        //mainPanel.setPreferredSize(new Dimension(400, 300));

        //progress bar
        progressBar.setVisible(false);
        progressLabel.setText("Ready");

        contentPanel.setVisible(true);
        setContentPane(contentPanel);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onLeave();
            }
        });
        setVisible(true);
        pack();

        onLoad();
    }


    private void onLeave() {
        boolean shouldClose = false;
        if (!saved) {
            final int response = JOptionPane.showConfirmDialog(contentPanel,
                    "Are you sure to leave without saving current solution?",
                    "Leave Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (response == JOptionPane.YES_OPTION)
                shouldClose = true;
        }
        if (saved || shouldClose)
            System.exit(0);
    }

    public void onLoad() {
        setSaved();
        openAction();
    }


    public void openAction() {
        String url = null;
        try {
            url = JOptionPane.showInputDialog(contentPanel,
                    "Please enter the url of the GDoc",
                    "Import timetable data",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            if (!(e instanceof ClassNotFoundException))
                throw e;
        }
        if (url == null || url.trim().length() < 1) return;
        progressBar.setValue(20);
        progressBar.setVisible(true);
        if (EventHandler.importData(url)) {
            buttonReset.setVisible(false);
            progressLabel.setText("OK! Imported timetable from " + url);
            progressBar.setValue(100);
            progressBar.setVisible(false);
        } else {
            progressBar.setValue(0);
            progressBar.setVisible(false);
            try {
                JOptionPane.showConfirmDialog(contentPanel,
                        Debug.ERROR_CODE_FILE_READ,
                        "Failed to open Excel file",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
            }
            progressLabel.setText(Debug.ERROR_MESSAGE_FILE_READ);
        }
    }

    public void saveAction() {
        String filename = JOptionPane.showInputDialog(contentPanel,
                "Please enter the full-path of the output Excel file",
                "Import timetable data",
                JOptionPane.INFORMATION_MESSAGE);
        if (filename == null || filename.trim().length() < 1) return;
        if (EventHandler.exportData(filename)) {
            setSaved();
            progressLabel.setText("OK! Saved solution to " + filename);
        } else {
            JOptionPane.showConfirmDialog(contentPanel,
                    Debug.ERROR_MESSAGE_FILE_READ,
                    "Failed to open Excel file",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean saved;

    public void setSaved() {
        saved = true;
        buttonSave.setEnabled(false);
    }

    public void setUnsaved() {
        saved = false;
        buttonSave.setEnabled(true);
    }

    private void changeStatus(final JButton button) {
        switch (button.getText()) {
            case GENERATE_START:
                button.setText(GENERATE_PAUSE);
                button.setToolTipText(GENERATE_PAUSE_TOOLTIPS);
                buttonReset.setVisible(true);
                setUnsaved();
                EventHandler.resetGenerate();
                EventHandler.resumeGenerate();
                break;
            case GENERATE_PAUSE:
                button.setText(GENERATE_RESUME);
                button.setToolTipText(GENERATE_RESUME_TOOLTIPS);
                EventHandler.pauseGenerate();
                break;
            case GENERATE_RESUME:
                button.setText(GENERATE_PAUSE);
                button.setToolTipText(GENERATE_PAUSE_TOOLTIPS);
                setUnsaved();
                EventHandler.resumeGenerate();
                break;
            default:
                System.err.println("Unexpected button clicked: " + button.getText());
        }
    }

    private JPanel lastSpreadSheetViewerPanel = null;

    public void updateSpreadSheetViewerPanel(JPanel jPanel) {
        if (lastSpreadSheetViewerPanel != null)
            mainPanel.remove(lastSpreadSheetViewerPanel);
        lastSpreadSheetViewerPanel = jPanel;
        mainPanel.add(jPanel, BorderLayout.CENTER);
    }
}
