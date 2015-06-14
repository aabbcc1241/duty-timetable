package dutytimetable.view

import java.util.function.Consumer
import javafx.scene.control.TextInputDialog

import dutytimetable.controller.ActionCancelException
import dutytimetable.model.FileModel

/**
 * Created by beenotung on 6/14/15.
 */
object FileView {
  @throws(classOf[ActionCancelException])
  def getUrl = {
    StatusView.saveAndBind(FileModel)

    FileModel.updateMessage("loading input dialog")
    FileModel.updateProgressStage(FileModel.Stage_PrepareDialog)

    val dialog = new TextInputDialog()
    dialog.setTitle("Import timetable data")
    dialog.setHeaderText("Please enter the url of the GDoc")
    dialog.setContentText("url")

    FileModel.updateMessage("waiting input from user")
    FileModel.updateProgressStage(FileModel.Stage_WaitInput)

    val result = dialog.showAndWait()
    result.ifPresent(new Consumer[String] {
      override def accept(t: String): Unit = {
        val url = t.trim
        if (url.length > 0)
          FileModel.openUrl(url)
      }
    })

    StatusView.restoreBind
  }
}
