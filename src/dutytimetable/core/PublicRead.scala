package dutytimetable.core

import java.util.function.Consumer

import com.google.gdata.client.spreadsheet.{FeedURLFactory, SpreadsheetService}
import com.google.gdata.data.spreadsheet._
import dutytimetable.core.logic.TimeSlot

import scala.collection.mutable.ListBuffer

//import scala.collection.JavaConversions._

import dutytimetable.debug.Debug

/**
 * Created by beenotung on 5/28/15.
 */
object PublicRead {
  val key = "15Zs-LJz8msbCQi7K-tY1v0heh76_99xwfaadIe3ZQkY"
  val APP_NAME = "Public Read Test"

  Debug.println("try get default URL")
  val url = FeedURLFactory.getDefault.getWorksheetFeedUrl(key, "public", "basic")
  Debug.println("success get default URL")
  val service = new SpreadsheetService(APP_NAME)
  val worksheetFeed = service.getFeed[WorksheetFeed](url, classOf[WorksheetFeed])

  /*(1 to 5 ).foreach(weekDay=>{
    val cellFeedUrl=worksheetFeed.getEntries.get(weekDay).getCellFeedUrl
    val cellFeed = service.getFeed[CellFeed](cellFeedUrl, classOf[CellFeed])
    //println(cellFeed.getEntries.size())
    cellFeed.getEntries.forEach(new Consumer[CellEntry] {
      override def accept(cellEntry: CellEntry): Unit = {
        val cell=cellEntry.getCell
        println()
        println(cellEntry)
        println(cellEntry.)
        if (cell!=null){
          println("")
          println("cell row: " + cell.getRow)
          println("cell col: " + cell.getCol)
          println("cell value: " + cell.getValue)
        }
      }
    })
  })*/

  /*get timeslot headers*/
  var timeSlotHeader: Array[String] = null

  {
    val timeSlotHeaderBuffer = new ListBuffer[String]
    service.getFeed(worksheetFeed.getEntries.get(0).getListFeedUrl, classOf[ListFeed]).getEntries.forEach(new Consumer[ListEntry] {
      override def accept(listEntry: ListEntry): Unit = {
        timeSlotHeaderBuffer += listEntry.getTitle.getPlainText
      }
    })
    timeSlotHeader = timeSlotHeaderBuffer.filter(str => str.last == '0').toArray[String]
  }
  println(timeSlotHeader.toVector)

  (1 to 1).foreach(weekDay => {
    val listFeed = service.getFeed(worksheetFeed.getEntries.get(weekDay).getListFeedUrl, classOf[ListFeed])
    listFeed.getEntries.forEach(new Consumer[ListEntry] {
      override def accept(listEntry: ListEntry): Unit = {
        val title = listEntry.getTitle.getPlainText
        println("title: " + listEntry.getTitle.getPlainText)
        println("header: " + "Roster")
        println("content: " + listEntry.getCustomElements.getValue("Roster"))
        println("header: " + timeSlotHeader(0))
        println("content: " + listEntry.getCustomElements.getValue(timeSlotHeader(0)))
      }
    })
  })

}

object test extends App {
  override def main(args: Array[String]) {
    PublicRead
  }
}