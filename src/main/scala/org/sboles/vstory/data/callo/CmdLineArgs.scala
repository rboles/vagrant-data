/**
 * Provides a command line argument parser for Callo
 *
 * See also RunTimeArgs
 *
 * @author sboles
 */

package org.sboles.vstory.data.callo

/**
 * Provides a command line argument parser for Callo
 */
class CmdLineArgs(args: Array[String]) {

  import CmdLineArgs.{stringArg,intArg,booleanArg,normalizeArgs}

  val normal = normalizeArgs(args)

  val mode:Option[String]  = stringArg(normal, "mode")
  val txt:Option[String]   = stringArg(normal, "txt")
  val sql:Option[String]   = stringArg(normal, "sql")
  val log:Option[String]   = stringArg(normal, "log")

  val help:Option[Boolean]  = booleanArg(normal, "help")
  val version:Option[Boolean]   = booleanArg(normal, "version")
}

/**
 * Companion object
 */
object CmdLineArgs {

  def modeList = List("armor-types", "armor-items", "armor-combos",
                      "weapon-types", "weapon-items", "weapon-combos",
                      "material-types", "material-combos")

  /**
   * Prints Seymour help message
   */
  def help: Unit = {
    println
    println("Callo ("+Callo.CALLO_VERSION+") generates SQL.")
    println
    println("Arguments")
    println
    println("mode     Run mode, see below")
    println("txt      Text data parent directory")
    println("sql      SQL destination parent directory")
    println("log      info, warn, debug (default: info)")
    println("version  Print version and exit")
    println("help     Print help and exit")
    println
    println("Callo transforms Vagrant Story text data into SQL statements.")
    println("Use the mode argument do direct Callo.")
    println("Use mode=all to run all modes")

    modeHelp
  }

  /**
   * Outputs list of supported modes
   */
  def modeHelp: Unit = {
    println
    println("Callo modes")
    println
    modeList.foreach(println _)
    println
  }

  /**
   * Output list of supported modes, prefixed by a message
   * @param msg Message
   */
  def modeHelp(msg: String): Unit = {
    println(msg)
    modeHelp
  }

  /**
   * Checks for a name/value pair and if found returns the value
   * as a String
   * @param args List of normalized args
   * @param name Argument name to check for
   * @return Some string or none
   */
  def stringArg(args: Array[(String, Option[String])], name: String): Option[String] = {
    var value: Option[String] = None
    for ( arg <- args ) {
      if ( arg._1 == name )
        arg._2 match {
          case Some(v) => value = Some(v)
          case _ =>
        }
    }
    value
  }

  /**
   * Checks for a name/value pair and if found returns the value
   * as an Int
   * @param args List of normalized args
   * @param name Argument name to check for
   * @return Some Int or none
   */
  def intArg(args: Array[(String, Option[String])], name: String): Option[Int] = {
    var value: Option[Int] = None
    for ( arg <- args ) {
      if ( arg._1 == name )
        arg._2 match {
          case Some(v) => value = Some(v.toInt)
          case _ =>
        }
    }
    value
  }

  /**
   * Just checks for existence of an argument by name
   * @param args List of normalized args
   * @param name Argument name to check for
   * @return Some true or none
   */
  def booleanArg(args: Array[(String, Option[String])], name: String): Option[Boolean] = {
    var value: Option[Boolean] = None
    for ( arg <- args ) {
      if ( arg._1 == name ) value = Some(true)
    }
    value
  }
  
  /**
   * Determines the delimiter type based on the argument prefix. The
   * argument may start with "/", "-" or nothing. If the argument starts
   * with "/" then ":" is assumed to be the delimiter. Otherwise, "=" is
   * the assumed delimiter.
   * @param arg Argument string
   * @return Argument delimiter.
   */
  def argDelimiter(arg: String): String = {
    arg(0) match {
      case '-' => "="
      case '/' => ":"
      case _ => "="
    }
  }

  /**
   * Parses an argument string for an argument name. 
   * @param arg Argument string
   * @return Argument name
   */
  def argName(arg: String): String = {
    val n = splitArg(arg, argDelimiter(arg))._1

    n(0) match {
      case '-' => n.substring(n.indexOf("-")+1)
      case '/' => n.substring(n.indexOf("/")+1)
      case _ => n
    }
  }

  /**
   * Parses an argument string for an argument value. Note that an argument
   * string may not have a value (it can just be a name)
   * @param arg Argument string
   * @return Some value or None
   */
  def argValue(arg: String): Option[String] = splitArg(arg, argDelimiter(arg))._2

  /**
   * Splits an argument into a name value pair. Note that an argument
   * always has a name.
   * @param delimiter Argument delimiter: ":" or "=" or nothing
   * @return List containing a name (always) and optionally a value
   */
  def splitArg(arg: String, delimiter: String): (String, Option[String]) = {
    arg.contains(delimiter) match {
      case true => {
        (arg.substring(0,arg.indexOf(delimiter)),
         Some(arg.substring(arg.indexOf(delimiter)+1)))
      }
      case false => {
        (arg, None)
      }
    }
  }

  /**
   * Normalizes arguments into an array of tuples: (name, Some(value))
   * After the arguments are normalized, any prefix character is removed
   * from the name and delimiters are stripped from name/value pairs
   * @param args Array of arguments
   * @return Array of tuples: (name, Some(value))
   */
  def normalizeArgs(args: Array[String]): Array[(String, Option[String])] = {
    args.map(arg => splitArg(arg, argDelimiter(arg))).map(arg => (argName(arg._1), arg._2))
  }
}
