/**
 * Reads the material type data file and writes SQL
 *
 * @author sboles
 */

package org.sboles.vstory.data.callo.material

import java.io.{File, IOException}
import java.io.{BufferedWriter, FileWriter}
import java.io.{BufferedReader, FileReader, File}

import scala.collection.mutable.Queue

import org.apache.log4j.Logger

object MaterialType {

  val logger = Logger.getLogger(classOf[MaterialType])

  /**
   * Reads material type text data, writes SQL statements
   * @param in Data source file
   * @param out SQL destination file
   */
  def readTxtWriteSql(in: File, out: File): Boolean = {
    val reader = try {
      Option(new BufferedReader(new FileReader(in)))
    } catch {
      case e => {
        logger.error("Failed to init data source: "+in)
        None
      }
    }

    val writer = try {
      Option(new BufferedWriter(new FileWriter(out)))
    } catch {
      case e => {
        logger.error("Failed to init SQL destination: "+out)
        None
      }
    }

    val success = ( reader match {
      case Some(r) => true
      case None => false
    } ) && ( writer match {
      case Some(w) => true
      case None => false
    } )

    success match {
      case true => {
        val br = reader match { case Some(r) => r; case _ => null }
        val bw = writer match { case Some(w) => w; case _ => null }
        readWrite(br, bw)
      }
      case false => false
    }
  }

  def tableName = "material"

  def deleteStmt(table: String): String = "delete from "+table+";"

  def alterStmt(table: String): String  = "alter table "+table+" AUTO_INCREMENT=1;"

  def insertStmt(table: String, name: String): String =
    "insert into "+table+" (name) values '"+name+"';"

  /**
   * Read data elements a line at a time and write SQL statements
   * @param br Buffered reader
   * @param bw Buffered writer
   * @return Count of data elements read
   */
  private def readWrite(br: BufferedReader, bw: BufferedWriter): Boolean = {

    def writeStmt(s: String): Unit = { bw.write(s); bw.newLine }

    def writeSql(sql: Seq[String]): Unit = sql.foreach(writeStmt _)

    var q = new Queue[String]
    var count = 0

    q += deleteStmt(tableName)

    q += alterStmt(tableName)

    var l = br.readLine

    while ( l != null ) {
      val process = ( l.length > 0 ) &&
      (l.substring(0,1) match {
        case "#" => false
        case _ => true
      })

      if ( process ) {
        count += 1
        q += insertStmt(tableName, l)

        if ( q.length == 5 ) {
          writeSql(q.toList)
          q = new Queue[String]
        }
      }

      l = br.readLine
    }

    if ( q.length > 0 ) writeSql(q.toList)

    logger.info("Found "+count+" material types")

    br.close
    bw.close

    count match {
      case 0 => false
      case _ => true
    }
  }
}

class MaterialType { }

