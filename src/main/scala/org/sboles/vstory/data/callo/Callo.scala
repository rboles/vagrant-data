/**
 * Callo parses Vagrant Story data files and generates SQL
 *
 * @author sboles
 */

package org.sboles.vstory.data.callo

import java.io.File

import org.apache.log4j.{Level, Logger}
import org.apache.log4j.{ConsoleAppender, FileAppender, PatternLayout}

import org.sboles.vstory.data.callo.worker.ItemWorker
import org.sboles.vstory.data.callo.worker.TypeWorker
import org.sboles.vstory.data.callo.worker.ComboWorker

object Callo {

  val CALLO_VERSION = "1.0"

  val logger = Logger.getLogger("org.sboles.vstory.data.callo.Callo")

  def main(args: Array[String]): Unit = {

    val runTimeArgs = new RunTimeArgs(new CmdLineArgs(args))

    if ( runTimeArgs.help ) {
      CmdLineArgs.help
      sys.exit
    }

    if ( runTimeArgs.version ) {
      println
      println("Callo version: "+CALLO_VERSION)
      println
      sys.exit
    }

    val mode = runTimeArgs.mode match {
      case Some(v) => v
      case _ => {
        CmdLineArgs.modeHelp("ERROR: Missing required argument: mode")
        sys.exit
      }
    }

    if ( !initLogging(runTimeArgs) ) sys.exit
    
    mode match {
      case "armor-items"     => run(mode, runTimeArgs)
      case "weapon-items"    => run(mode, runTimeArgs)
      case "armor-types"     => run(mode, runTimeArgs)
      case "material-types"  => run(mode, runTimeArgs)
      case "weapon-types"    => run(mode, runTimeArgs)
      case "armor-combos"    => run(mode, runTimeArgs)
      case "material-combos" => run(mode, runTimeArgs)
      case "weapon-combos"   => run(mode, runTimeArgs)
      case "all" => CmdLineArgs.modeList.foreach(run(_, runTimeArgs))
      case _ => run(runTimeArgs)
    }
  }
  
  /**
   * This method is provided to support non-standard modes
   * should the necessity arise
   * @param args Run time args
   */
  def run(args: RunTimeArgs): Unit = { }
  
  /**
   * Run a specific mode
   * @param mode Run mode
   * @param args Run time args
   */
  def run(mode: String, args: RunTimeArgs): Unit = {
    mode match {
      case "armor-items" => {
        val worker = new ItemWorker("armor")
        worker.work(args)
      }
      case "weapon-items" => {
        val worker = new ItemWorker("weapon")
        worker.work(args)
      }
      case "armor-types" => {
        val worker = new TypeWorker("armor")
        worker.work(args)
      }
      case "material-types" => {
        val worker = new TypeWorker("material")
        worker.work(args)
      }
      case "weapon-types" => {
        val worker = new TypeWorker("weapon")
        worker.work(args)
      }
      case "armor-combos" => {
        val worker = new ComboWorker("armor")
        worker.work(args)
      }
      case "material-combos" => {
        val worker = new ComboWorker("material")
        worker.work(args)
      }
      case "weapon-combos" => {
        val worker = new ComboWorker("weapon")
        worker.work(args)
      }
      case _ => logger.error("Unrecognized mode: "+mode)
    }
  }

  /**
   * Initialize logging
   * @param args Run time arguments
   */
  def initLogging(args: RunTimeArgs): Boolean = {
    args.logLevel match {
      case "debug" => Logger.getRootLogger.setLevel(Level.DEBUG)
      case "info" => Logger.getRootLogger.setLevel(Level.INFO)
      case _ => Logger.getRootLogger.setLevel(Level.INFO)
    }

    try {
      val pattern = "%-4r %d [%t] %-5p - %m%n"
      val layout = new PatternLayout(pattern)
      val appender = new ConsoleAppender(layout)
      Logger.getRootLogger.addAppender(appender)
      true
    } catch {
      case e => {
        println("ERROR: Failed to initialize logging: "+e.getMessage)
      }
      false
    }
  }
}
