package dutytimetable.gui;

import dutytimetable.debug.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Created by hkpu on 15/5/2015.
 */
public abstract class BaseForm {
    JPanel contentPanel;
    private JToolBar mainToolBar;
    private JPanel mainPanel;
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

    public BaseForm() {
        buttonOpen = new JButton("Open");
        buttonOpen.setToolTipText("Load from excel file");
        buttonSave = new JButton("Save");
        buttonSave.setToolTipText("Save to excel file");
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
                resetGenerate();
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

        mainToolBar.add(buttonOpen);
        mainToolBar.add(buttonSave);
        mainToolBar.addSeparator(new Dimension(50, 1));
        mainToolBar.add(buttonGenerate);
        mainToolBar.add(buttonReset);
        mainToolBar.addSeparator(new Dimension(50, 1));
        mainToolBar.add(buttonExit);

        mainPanel.setPreferredSize(new Dimension(400, 300));
        progressBar.setVisible(false);
        progressLabel.setText("Ready");
        System.out.println(mainPanel.getSize());

        onLoad();
    }

    private void onLeave() {

    }

    public void onLoad() {
        contentPanel.setVisible(true);
        setSaved();
        openAction();
    }

    public abstract void pauseGenerate();

    public abstract void resumeGenerate();

    public abstract void resetGenerate();

    public abstract boolean importData();

    public abstract boolean exportData(String filename);

    public void openAction() {
        String filename = JOptionPane.showInputDialog(contentPanel,
                "Please enter the full-path of the source Excel file",
                "Import timetable data",
                JOptionPane.INFORMATION_MESSAGE);
        if (filename == null || filename.trim().length() < 1) return;
        if (importData()) {
            buttonReset.setVisible(false);
            progressLabel.setText("OK! Imported timetable from " + filename);
        } else {
            JOptionPane.showConfirmDialog(contentPanel,
                    Debug.ERROR_MESSAGE_FILE_READ,
                    "Failed to open Excel file",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveAction() {
        String filename = JOptionPane.showInputDialog(contentPanel,
                "Please enter the full-path of the output Excel file",
                "Import timetable data",
                JOptionPane.INFORMATION_MESSAGE);
        if (filename == null || filename.trim().length() < 1) return;
        if (exportData(filename)) {
            setSaved();
            progressLabel.setText("OK! Saved solution to "+filename);
        } else
        {
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
                resetGenerate();
                resumeGenerate();
                break;
            case GENERATE_PAUSE:
                button.setText(GENERATE_RESUME);
                button.setToolTipText(GENERATE_RESUME_TOOLTIPS);
                pauseGenerate();
                break;
            case GENERATE_RESUME:
                button.setText(GENERATE_PAUSE);
                button.setToolTipText(GENERATE_PAUSE_TOOLTIPS);
                setUnsaved();
                resumeGenerate();
                break;
            default:
                System.err.println("Unexpected button clicked: " + button.getText());
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("BaseForm");
        frame.setContentPane(new BaseForm() {
            @Override
            public void pauseGenerate() {
                System.out.println("pauseGenerate");
            }

            @Override
            public void resumeGenerate() {
                System.out.println("resumeGenerate");
            }

            @Override
            public void resetGenerate() {
                System.out.println("resetGenerate");
            }

            @Override
            public boolean importData() {
                System.out.println("importData");
                return new Random(System.currentTimeMillis()).nextBoolean();
            }

            @Override
            public boolean exportData(String filename) {
                System.out.println("exportData");
                return new Random(System.currentTimeMillis()).nextBoolean();
            }
        }.contentPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
