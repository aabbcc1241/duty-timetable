package dutytimetable.gui;

import dutytimetable.debug.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * Created by hkpu on 15/5/2015.
 */
public abstract class BaseJFrame extends JFrame {
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

    public BaseJFrame() {
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
            Object[]options={"OK"};
            try {
                JOptionPane.showConfirmDialog(contentPanel,
                        (Object)Debug.ERROR_MESSAGE_FILE_READ,
                        "Failed to open Excel file",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,//icon
                        options,null
                );
            }catch (HeadlessException e){
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
        if (exportData(filename)) {
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
               BaseJFrame baseJFrame=new BaseJFrame() {
            @Override
            public void pauseGenerate() {
                System.out.println("pause iterate");
            }

            @Override
            public void resumeGenerate() {
                System.out.println("resume iterate");
            }

            @Override
            public void resetGenerate() {
                System.out.println("reset iterate");
            }

            @Override
            public boolean importData() {
                System.out.println("import data");
                return new Random().nextBoolean();
            }

            @Override
            public boolean exportData(String filename) {
                System.out.println("export data");
                return new Random().nextBoolean();
            }
        };
    }


}
