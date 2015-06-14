package dutytimetable.model

import java.io.IOException

import dutytimetable.controller.ActionCancelException
import dutytimetable.model.Config
import dutytimetable.view.StatusView
import myutils.FileUtils
import myutils.debug.Debug
import myutils.google.GoogleUtils

/**
 * Created by beenotung on 6/13/15.
 */
object FileModel extends ProgressiveModelTrait {
  val Stage_PrepareDialog = 0
  val Stage_WaitInput = Stage_PrepareDialog + 1
  val Stage_DecodeKey = Stage_WaitInput + 1
  val Stage_GenerateDownloadUrl = Stage_DecodeKey + 1
  val Stage_Download = Stage_GenerateDownloadUrl + 1
  val Stage_Import = Stage_Download + 1
  override val Stage_Final: Double = Stage_Import

  @throws(classOf[ActionCancelException])
  def openUrl(url: String): Unit = {
    Debug.showMessage("openUrl")
    StatusView.saveAndBind(this)

    updateMessage("decoding key")
    updateProgressStage(FileModel.Stage_DecodeKey)
    val key = GoogleUtils.extractDocKey(url)

    updateMessage("generating download url from key [" + key + "]")
    updateProgressStage(FileModel.Stage_GenerateDownloadUrl)
    val fileUrl = GoogleUtils.generateDocExportUrl(key, Config.FILE_FORMAT)

    updateMessage("downloading from " + fileUrl)
    updateProgressStage(FileModel.Stage_Download)
    try {
      FileUtils.downloadFile(fileUrl, Config.FILENAME)
    } catch {
      case e: IOException =>
        throw new ActionCancelException(new Runnable {
          override def run(): Unit = {
            StatusView.savedTargets.clear()
            GlobalStatus.bindMeOnStatusView
            GlobalStatus.updateMessage(Debug.ERROR_PAIR_FILE_DOWNLOAD.errorMessage)
            GlobalStatus.updateProgressStage(0)
          }
        })
    }

    updateMessage("importing from " + Config.FILENAME)
    updateProgressStage(FileModel.Stage_Import)
    Thread.sleep(5000)

    StatusView.restoreBind
  }

  var saved = true

  def saveFile = {
    //TODO
    if (!saved) {
      println("saving file")
      saved = true
    }
  }


}
