package dutytimetable.core.businesslogic

/**
 * Created by beenotung on 5/28/15.
 */
object Worker {
  val DEFAULT_STATUS = 3
}

class Worker(name: String) {
  val timeSlot = Array.fill[Int](17)(Worker.DEFAULT_STATUS)
}
object test extends App{
  override def main(args: Array[String]) {
    println(new Worker("name").timeSlot.toVector)
  }
}
