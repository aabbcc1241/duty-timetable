package dutytimetable.model.businesslogic

/**
 * Created by beenotung on 5/28/15.
 */
object TimeSlot {
  /**
   * Time Slot Name start from 0 to 16 map from 10:00-10:30 to 18:00-18:30
   */
  val TimeSlotName = Array.tabulate[String](17)(i => {
    val startMinute = (i % 2) * 30
    val startHour = i / 2 + 10
    val endMinute = (startMinute + 30) % 60
    val endHour = startHour + startMinute / 30
    startHour + ":" + {
      if (startMinute == 0) "00" else startMinute
    } + "-" + endHour + ":" + {
      if (endMinute == 0) "00" else endMinute
    }
  })

  def getTimeSlotColumnHeader(timeSlotIndex: Int): String = {
    //TODO
    " "
  }
}