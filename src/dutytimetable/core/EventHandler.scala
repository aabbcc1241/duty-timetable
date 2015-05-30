package dutytimetable.core

import java.util.Random

/**
 * Created by beenotung on 5/30/15.
 */
object EventHandler {
  def pauseGenerate {
    System.out.println("pause iterate")
  }

  def resumeGenerate {
    System.out.println("resume iterate")
  }

  def resetGenerate {
    System.out.println("reset iterate")
  }

  def importData: Boolean = {
    System.out.println("import data")
    return new Random().nextBoolean
  }

  def exportData(filename: String): Boolean = {
    System.out.println("export data")
    return new Random().nextBoolean
  }
}
