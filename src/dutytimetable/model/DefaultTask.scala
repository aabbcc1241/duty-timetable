package dutytimetable.model

import javafx.beans.value.{ObservableValue, ChangeListener}

/**
 * Created by beenotung on 6/14/15.
 */
object DefaultTask extends TransparentTask[Void]{
  override def call(): Void = ???
  updateMessage("")
  updateProgress(1)
}