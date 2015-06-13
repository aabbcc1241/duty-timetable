package dutytimetable.gui.fx.mainmodel

import java.util.function.Consumer
import javafx.scene.control.TextInputDialog

/**
 * Created by beenotung on 6/13/15.
 */
object GDoc {
  def receiveUrl(url: String) = {
    printf("url=" + url)
  }

  def getUrl = {
    val dialog = new TextInputDialog()
    dialog.setTitle("Import timetable data")
    dialog.setHeaderText("Please enter the url of the GDoc")
    dialog.setContentText("url")

    val result = dialog.showAndWait()
    result.ifPresent(new Consumer[String] {
      override def accept(t: String): Unit = receiveUrl(t)
    })
  }
}
