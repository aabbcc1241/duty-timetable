package dutytimetable.controller

/**
 * Created by beenotung on 6/14/15.
 */
class ActionCancelException(val runnable: Runnable) extends UnsupportedOperationException {
  override def getMessage: String = "ActionCancelException"
}
