package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.worker {

    import scala.collection.mutable.HashMap

    import java.io.{BufferedWriter,File,FileWriter}

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.ComboData
    import org.sboles.vstory.data.callo.parser.ComboParser
    import org.sboles.vstory.data.callo.parser.MaterialComboParser
    import org.sboles.vstory.data.callo.printer.ArmorComboPrinter
    import org.sboles.vstory.data.callo.printer.ComboPrinter
    import org.sboles.vstory.data.callo.printer.MaterialComboPrinter
    import org.sboles.vstory.data.callo.printer.WeaponComboPrinter

    /**
     * Provides a worker that builds combo type SQL
     *
     * @author sboles
     */
    class ComboWorker extends Worker {

      def this(objClass: String) {
        this
        _objectClass = Some(objClass)
      }

      private val _logger = Logger.getLogger(classOf[ComboWorker])

      override def logger: Logger = _logger

      /**
       * @param args Run time arguments
       */
      def work(args: RunTimeArgs): Unit = {
        val comboClass = objectClass(args)

        comboClass match {
          case "armor" => createComboSql(args)
          case "weapon" => createComboSql(args)
          case "material" => createMaterialComboSql(args)
        }
      }

      /**
       * Print combos to console
       * @param printer Combo printer
       * @param combos List of combos
       */
      def debug(printer: ComboPrinter, combos: List[ComboData]): Unit = {
        def toConsole(s: String): Unit = logger.info(s)

        printer.toTxt(combos, toConsole)

        logger.info("Found "+combos.length+" combos")
      }

      /**
       * Generic combo SQL builder that works for armor and weapons
       * @param args Run time arguments
       */
      def createComboSql(args: RunTimeArgs): Unit = {
        val comboClass = objectClass(args)

        val inputFileName = comboClass + "-combos.txt"
        val outputFileName = comboClass + "-combos.sql"

        val parser = new ComboParser

        logger.info("Creating "+comboClass+" combo SQL")

        val txt = args.txt+File.separator+comboClass+File.separator
        val sql = args.sql+File.separator+comboClass+File.separator

        val in = new File(txt+inputFileName)
        val out = new File(sql+outputFileName)

        logger.info("TXT input:  "+in.getPath)

        val combos = parser.parse(in)

        val printer = comboClass match {
          case "armor" => new ArmorComboPrinter
          case "weapon" => new WeaponComboPrinter
        }

        debug(printer, combos)

        val bw = new BufferedWriter(new FileWriter(out))

        def fileWrite(s: String): Unit = {
          bw.write(s)
          bw.newLine
        }

        printer.toSql(combos, fileWrite)

        bw.close

        logger.info("Wrote: "+out.getPath)
      }

      /**
       * SQL builder specific to material combo data
       * @param args Run time arguments
       */
      def createMaterialComboSql(args: RunTimeArgs): Unit = {
        val comboClass = "material"

        val srcs = List("material-combos-armor.txt",
                        "material-combos-weapon.txt")

        val parser = new MaterialComboParser

        val txt = args.txt+File.separator+comboClass+File.separator
        val sql = args.sql+File.separator+comboClass+File.separator

        for ( src <- srcs ) {
          val in  = new File(txt+src)
          val itemType = src.split('.')(0).split('-')(2)
          val outputFileName = comboClass + "-combos-"+itemType+".sql"
          val out = new File(sql+outputFileName)

          logger.info("TXT input: "+in.getPath)

          val combos = parser.parse(in)

          val printer = new MaterialComboPrinter(itemType)

          debug(printer, combos)

          val bw = new BufferedWriter(new FileWriter(out))

          def fileWrite(s: String): Unit = {
            bw.write(s)
            bw.newLine
          }

          printer.toSql(combos, fileWrite)

          bw.close

          logger.info("Wrote: "+out.getPath)
        }
      }
    }
  }
}
