package dutytimetable.core

import java.util.Random

import dutytimetable.debug.Debug
import dutytimetable.gui.{SpreadSheetViewer, CoreJFrame}
import myutils.FileUtils
import myutils.google.GoogleUtils

/**
 * Created by beenotung on 5/30/15.
 */
object EventHandler {
  def pauseGenerate {
    System.out.println("pause iterate")
  }

  def resumeGenerate {
    System.out.println("resume iterate")
  }

  def resetGenerate {
    System.out.println("reset iterate")
  }

  def importData(url: String): Boolean = {
    import Facilitator.FILE_FORMAT
    import Facilitator.FILENAME
    val fileUrl = GoogleUtils.generateDocExportUrl(GoogleUtils.extractDocKey(url), FILE_FORMAT)
    println(fileUrl)
    if (FileUtils.downloadFile(fileUrl, FILENAME) != FILENAME) return false
    val viewerPanel = SpreadSheetViewer.getViewerPanel(FILENAME)
    if (viewerPanel == null) return false
    //CoreJFrame.getInstance().mainPanel.setUI(viewerPanel.getUI)
    //CoreJFrame.getInstance().mainPanel.updateUI()
    CoreJFrame.getInstance().updateSpreadSheetViewerPanel(viewerPanel)
    Debug.showMessage("imported success")
    true
  }

  def exportData(filename: String): Boolean = {
    System.out.println("export data")
    return new Random().nextBoolean
  }
}
