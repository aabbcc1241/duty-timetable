package dutytimetable.utils

import java.io.FileInputStream
import java.util.function.Consumer

import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.xssf.usermodel.XSSFWorkbook


/**
 * Created by beenotung on 5/31/15.
 */
object ExcelReader {
  def preview(filename: String): Unit = {
    val in = new FileInputStream(filename)
    val workbook = new XSSFWorkbook(in)
    var sheet = workbook.getSheetAt(0)

    sheet.rowIterator().forEachRemaining(new Consumer[Row] {
      override def accept(row: Row): Unit = {
        println()
        println("row#=" + row.getRowNum)
        println(row.cellIterator().forEachRemaining(new Consumer[Cell] {
          override def accept(cell: Cell): Unit = {
            print("\t")
            if (cell.getCellType == Cell.CELL_TYPE_NUMERIC) print(cell.getNumericCellValue)
            else print(cell.getStringCellValue)

          }
        }))
      }
    })
  }
}
