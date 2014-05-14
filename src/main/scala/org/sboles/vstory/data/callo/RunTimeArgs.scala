/**
 * Provides a container and defaults for run time arguments
 */

package org.sboles.vstory.data.callo

/**
 * Provides argument defaults
 */
object RunTimeArgs {

  val DEFAULT_TXT_DIRECTORY = "../data/txt"

  val DEFAULT_SQL_DIRECTORY = "../data/sql"

}

/**
 * Initializes run time arguments from command line arguments and
 * defaults
 */
class RunTimeArgs(args: CmdLineArgs) {

  import RunTimeArgs._

  val mode: Option[String] = args.mode

  val txt = args.txt match {
    case Some(v) => v
    case _ => DEFAULT_TXT_DIRECTORY
  }

  val sql = args.sql match {
    case Some(v) => v
    case _ => DEFAULT_SQL_DIRECTORY
  }

  val help = args.help match {
    case Some(v) => true
    case _ => false
  }

  val version = args.version match {
    case Some(v) => true
    case _ => false
  }

  val logLevel = args.log match {
    case Some(v) => v.toLowerCase
    case _ => "warn"
  }
}
