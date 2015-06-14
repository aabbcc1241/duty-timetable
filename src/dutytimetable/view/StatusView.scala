package dutytimetable.view

import javafx.beans.binding.DoubleExpression
import javafx.beans.value.{ChangeListener, ObservableValue}

import dutytimetable.controller.MainController
import dutytimetable.model.{GlobalStatus, ProgressiveListener, ProgressiveModelTrait}

import scala.collection.mutable

/**
 * Created by beenotung on 6/14/15.
 */

object StatusView extends ProgressiveListener {
  val instance: ProgressiveListener = this

  def controller: MainController = MainController.getInstance()

  def updateText(newVal: String) = controller.getStatusLabel.setText(newVal)

  def updateProgress(newVal: Double) = {
    controller.getProgressIndicator.progressProperty().unbind()
    controller.getProgressIndicator.setProgress(newVal)
  }

  def updateDirectly(progressiveModel: ProgressiveModelTrait) = {
    updateProgress(progressiveModel.task.getProgress)
    updateText(progressiveModel.task.getMessage)
  }

  def setProgressBind(newDoubleExpression: DoubleExpression) = controller.getProgressIndicator.progressProperty().bind(newDoubleExpression)

  val textListener: ChangeListener[String] = new ChangeListener[String] {
    override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {
      updateText(newValue)
    }
  }

  def unBind(oldTarget: ProgressiveModelTrait) = {
    target = null
    oldTarget.progressiveListeners
    oldTarget.progressiveListeners -= this
    controller.getProgressIndicator.progressProperty().unbind()
    oldTarget.task.messageProperty().removeListener(textListener)
    GlobalStatus.bindMeOnStatusView
  }

  var target: ProgressiveModelTrait = null

  override def notified(progressiveModel: ProgressiveModelTrait): Unit = {
    if (target != null) unBind(target)
    target = progressiveModel

    updateDirectly(progressiveModel)
    setProgressBind(progressiveModel.task.progressProperty())
    progressiveModel.task.messageProperty().addListener(textListener)
  }

  var savedTargets = new mutable.Stack[ProgressiveModelTrait]()


   
  def saveBind = {
    savedTargets.push(target)
  }

  def saveAndBind(newTarget: ProgressiveModelTrait)={
    saveBind
    newTarget.bindMeOnStatusView
  }

  def restoreBind = {
    if (savedTargets.isEmpty)
      GlobalStatus.bindMeOnStatusView
    else
      savedTargets.pop().bindMeOnStatusView
  }

}
