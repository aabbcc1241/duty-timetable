package dutytimetable.model

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

  def openUrl(url: String) = {
    FileModel.task.updateMessage("decode key")
    FileModel.updateProgressStage(FileModel.Stage_DecodeKey)
    printf("url=" + url)
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
