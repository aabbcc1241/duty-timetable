package dutytimetable.gui

import java.awt.BorderLayout
import java.io.{OutputStream, PrintStream}
import javax.swing.{JFrame, JPanel, WindowConstants}

import dutytimetable.debug.Debug
import myutils.lang.Timer
import org.jopendocument.model.OpenDocument
import org.jopendocument.panel.ODSViewerPanel
import org.jopendocument.print.DefaultDocumentPrinter

/**
 * Created by beenotung on 5/29/15.
 */
object SpreadSheetViewer {
  var _active = false

  def active: Boolean = _active

  def active_=(newVal: Boolean) = _active = newVal

  def getViewerPanel(path: String): ODSViewerPanel = {
    println("start getViewerPanel")
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
      //viewerPanel = new ODSViewerPanel(doc,true)
      viewerPanel.updateUI()
    }
    finally {
      System.setErr(err)
      System.setOut(out)
    }
    println("finish getViewerPanel")
    viewerPanel
  }

  def showFile(path: String): Unit = {

    val mainFrame = new JFrame("Viewer")


    //mainFrame.setContentPane(getViewerPanel(path))
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    mainFrame.setLocation(10, 10)
    mainFrame.setVisible(true)
    Debug.showMessage("show Viewer Frame")
    active = true
    mainFrame.getContentPane.setLayout(new BorderLayout())
    val controlPanel = new JPanel()
    var contentPanel = getViewerPanel(path)
    mainFrame.getContentPane.add(controlPanel, BorderLayout.NORTH)
    mainFrame.getContentPane.add(contentPanel, BorderLayout.CENTER)
    var ve = 1
    Timer.setTimeInterval(callback = {
      Debug.showMessage("refresh Viewer")
      mainFrame.getContentPane.remove(contentPanel)
      Debug.showMessage("d----1")
      contentPanel = getViewerPanel(path)
      Debug.showMessage("d----2")
      mainFrame.getContentPane.add(contentPanel, BorderLayout.CENTER)
      //      Debug.showMessage("d----3")
      //      mainFrame.validate()
      //      Debug.showMessage("d----4")
      //      mainFrame.pack
      //mainFrame.repaint()
      Debug.showMessage("d----5")
      //contentPanel.repaint()
//      val size = mainFrame.getSize
//      val now = System.currentTimeMillis().toInt
//      mainFrame.setSize(
//        size.width + ve,
//        size.height + ve
//      )
//      ve *= -1
      contentPanel.invalidate()
      contentPanel.validate()
      Debug.showMessage("finish refresh Viewer")
    }, active, 1000)
  }
}

object SpreadSheetViewerTest extends App {
  override def main(args: Array[String]) {
    Debug.showMessage(System.getProperty("user.dir"))
    SpreadSheetViewer.showFile("duty-timetable/res/test.ods")
  }
}