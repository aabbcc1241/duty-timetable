package dutytimetable.gui

import java.io.{OutputStream, PrintStream}
import javax.swing.{JFrame, WindowConstants}

import dutytimetable.debug.Debug
import org.jopendocument.model.OpenDocument
import org.jopendocument.panel.ODSViewerPanel
import org.jopendocument.print.DefaultDocumentPrinter

/**
 * Created by beenotung on 5/29/15.
 */
object SpreadSheetViewer {
  def getViewerPanel(path: String): ODSViewerPanel = {
    val err = System.err
    val out = System.out
    System.setErr(new PrintStream(new OutputStream {
      override def write(b: Int): Unit = {}
    }))
    System.setOut(System.err)
    var viewerPanel: ODSViewerPanel = null
    try {
      var doc = new OpenDocument
      doc.loadFrom(path)
      val mainFrame = new JFrame("Viewer")
      val printer = new DefaultDocumentPrinter
      viewerPanel = new ODSViewerPanel(doc, printer, true)
      viewerPanel.updateUI()
    }
    finally {
      System.setErr(err)
      System.setOut(out)
    }
    viewerPanel
  }

  def showFile(path: String): Unit = {

    val mainFrame = new JFrame("Viewer")


    //mainFrame.setContentPane(getViewerPanel(path))
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    mainFrame.setLocation(10, 10)
    mainFrame.setVisible(true)
    Debug.showMessage("show Viewer Frame")
    while (mainFrame.isVisible) {
      mainFrame.setContentPane(getViewerPanel(path))
      mainFrame.pack
      Thread.sleep(100)
    }
    Debug.showMessage("Viewer Frame closed")
  }
}

object SpreadSheetViewerTest extends App {
  override def main(args: Array[String]) {
    Debug.showMessage(System.getProperty("user.dir"))
    SpreadSheetViewer.showFile("duty-timetable/res/test.ods")
  }
}