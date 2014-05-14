
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.worker {

    import java.io.{BufferedWriter,File,FileWriter}

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.ItemData
    import org.sboles.vstory.data.callo.parser.ItemParser
    import org.sboles.vstory.data.callo.printer.ItemPrinter
    import org.sboles.vstory.data.callo.printer.ArmorItemPrinter
    import org.sboles.vstory.data.callo.printer.WeaponItemPrinter

    /**
     * Provides a worker that builds item SQL
     *
     * @author sboles
     */
    class ItemWorker extends Worker {

      def this(objClass: String) {
        this
        _objectClass = Some(objClass)
      }

      private val _logger = Logger.getLogger(classOf[ItemWorker])

      override def logger: Logger = _logger

      /**
       * @param args Run time arguments
       */
      def work(args: RunTimeArgs): Unit = {
        val itemClass = objectClass(args)

        val inputFileName = itemClass + "-combos.txt"
        val outputFileName = itemClass + "-items.sql"

        val parser = new ItemParser

        logger.info("Creating "+itemClass+" item SQL")

        val txt = args.txt+File.separator+itemClass+File.separator
        val sql = args.sql+File.separator+itemClass+File.separator

        val in = new File(txt+inputFileName)
        val out = new File(sql+outputFileName)

        logger.info("TXT input:  "+in.getPath)

        val items = (new ItemParser).parse(in)

        val printer = itemClass match {
          case "armor" => new ArmorItemPrinter
          case "weapon" => new WeaponItemPrinter
        }

        debug(printer, items)

        val bw = new BufferedWriter(new FileWriter(out))

        def toSql(s: String): Unit = {
          bw.write(s)
          bw.newLine
        }

        printer.toSqlData(items, toSql)

        bw.close

        logger.info("Wrote: "+out.getPath)
      }

      def debug(printer: ItemPrinter, items: List[ItemData]): Unit = {
        def toConsole(s: String): Unit = logger.info(s)

        printer.toTxtData(items, toConsole)

        logger.info("Found "+items.length+" unique items")
      }
    }
  }
}
