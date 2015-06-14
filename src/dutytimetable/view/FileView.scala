package dutytimetable.view

import java.util.function.Consumer
import javafx.scene.control.TextInputDialog

import dutytimetable.controller.MainController
import dutytimetable.model.FileModel

/**
 * Created by beenotung on 6/14/15.
 */
object FileView {
  def getUrl = {
    FileModel.bindMeOnStatusView

    FileModel.task.updateMessage("loading input dialog")
    FileModel.updateProgressStage(FileModel.Stage_PrepareDialog)

    val dialog = new TextInputDialog()
    dialog.setTitle("Import timetable data")
    dialog.setHeaderText("Please enter the url of the GDoc")
    dialog.setContentText("url")

    FileModel.task.updateMessage("waiting input from user")
    FileModel.updateProgressStage(FileModel.Stage_WaitInput)

    val result = dialog.showAndWait()
    result.ifPresent(new Consumer[String] {
      override def accept(url: String): Unit =
        if (url.length > 0)
          FileModel.openUrl(url)
    })
  }
}
