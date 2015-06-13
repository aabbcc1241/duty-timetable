import javafx.application.Application
import javafx.concurrent.Task
import javafx.scene.Scene
import javafx.scene.control.ProgressBar
import javafx.scene.layout.StackPane
import javafx.stage.Stage

/**
 * Created by beenotung on 6/13/15.
 */
object Test extends App {
  override def main(args: Array[String]) {
    //println(Runtime.getRuntime.getClass)
    //println(this.getClass.getResource("Test.scala").toString)
    (new ProgressTest).main(args)
  }
}

class ProgressTest extends Application {
  def main(args: Array[String]) {
    Application.launch()
  }

  override def start(primaryStage: Stage): Unit = {
    val task = new Task[Void]() {
      override def call(): Void = {
        val N=100
        (1 to N).foreach(i => {
          Thread.sleep(5000/N)
          println(i)
          updateProgress(i, N)
        })
        null
      }
    }

    val progressBar = new ProgressBar()
    progressBar.progressProperty().bind(task.progressProperty())
    val thread = new Thread(task)
    thread.setDaemon(true)
    thread.start()

    val layout = new StackPane()
    layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;")
    layout.getChildren.add(progressBar)

    primaryStage.setScene(new Scene(layout))
    primaryStage.show()
  }
}
