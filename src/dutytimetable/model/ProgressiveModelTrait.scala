package dutytimetable.model

import dutytimetable.view.StatusView

import scala.collection.mutable.ArrayBuffer

/**
 * Created by beenotung on 6/14/15.
 */
trait ProgressiveModelTrait {
   val Stage_Final: Double

  def updateProgressStage(newStage: Double) = {
    task.updateProgress(newStage / Stage_Final)
  }

  var task: TransparentTask[Void] = new TransparentTask[Void] {
    override def call(): Void = null
  }

  def startTask(newTask: TransparentTask[Void]) = {
    task = newTask
    val thread = new Thread(task)
    thread.start()
  }

  def startAndWaitTask(newTask: TransparentTask[Void]) = {
    task = newTask
    val thread = new Thread(task)
    thread.run()
  }

  def bindMe(progressiveListener: ProgressiveListener) {
    progressiveListeners += progressiveListener
    progressiveListener.notified(this)
  }

  def bindMeOnStatusView {
    progressiveListeners += StatusView
    StatusView.notified(this)
  }

  val progressiveListeners = new ArrayBuffer[ProgressiveListener]

  def notifyProgressiveListener {
    progressiveListeners.foreach(p => p.notified(this))
  }
}
