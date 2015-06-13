/**
 * Created by beenotung on 6/13/15.
 */
object Test extends App{
override def main(args: Array[String]) {
  println(Runtime.getRuntime.getClass)
  println(this.getClass.getResource("Test.scala").toString)
}
}
