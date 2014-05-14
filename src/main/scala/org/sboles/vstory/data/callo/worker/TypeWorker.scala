
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.worker {

    import scala.collection.mutable.HashMap

    import java.io.{BufferedWriter,File,FileWriter}

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.MaterialTypeData
    import org.sboles.vstory.data.callo.datatype.TypeData
    import org.sboles.vstory.data.callo.parser.MaterialTypeParser
    import org.sboles.vstory.data.callo.parser.TypeParser
    import org.sboles.vstory.data.callo.printer.ArmorTypePrinter
    import org.sboles.vstory.data.callo.printer.MaterialTypePrinter
    import org.sboles.vstory.data.callo.printer.TypePrinter
    import org.sboles.vstory.data.callo.printer.WeaponTypePrinter

    /**
     * Provides a worker that builds object type SQL
     *
     * @author sboles
     */
    class TypeWorker extends Worker {

      def this(objClass: String) {
        this
        _objectClass = Some(objClass)
      }

      private val _logger = Logger.getLogger(classOf[TypeWorker])

      override def logger: Logger = _logger

      /**
       * @param args Run time arguments
       */
      def work(args: RunTimeArgs): Unit = {
        val typeClass = objectClass(args)

        typeClass match {
          case "armor" => createTypeSql(args)
          case "weapon" => createTypeSql(args)
          case "material" => createMaterialTypeSql(args)
        }
      }

      /**
       * Generic type SQL builder that works for armor and weapons
       * @param args Run time arguments
       */
      def createTypeSql(args: RunTimeArgs): Unit = {
        val typeClass = objectClass(args)

        val inputFileName = typeClass + "-combos.txt"
        val outputFileName = typeClass + "-types.sql"

        logger.info("Creating "+typeClass+" type SQL")

        val txt = args.txt+File.separator+typeClass+File.separator
        val sql = args.sql+File.separator+typeClass+File.separator

        val in = new File(txt+inputFileName)
        val out = new File(sql+outputFileName)

        logger.info("TXT input:  "+in.getPath)

        val parser = new TypeParser

        val types = parser.parse(in)

        val printer = typeClass match {
          case "armor" => new ArmorTypePrinter
          case "weapon" => new WeaponTypePrinter
        }

        debug(printer, types)

        val bw = new BufferedWriter(new FileWriter(out))

        def toSql(s: String): Unit = {
          bw.write(s)
          bw.newLine
        }

        printer.toSqlData(types, toSql)

        bw.close

        logger.info("Wrote: "+out.getPath)
      }

      /**
       * SQL builder specific to material type data
       * @param args Run time arguments
       */
      def createMaterialTypeSql(args: RunTimeArgs): Unit = {
        val typeClass = "material"

        val srcs = List("material-combos-armor.txt",
                        "material-combos-weapon.txt")

        val outputFileName = typeClass + "-types.sql"

        val parser = new MaterialTypeParser

        logger.info("Creating "+typeClass+" type SQL")

        val txt = args.txt+File.separator+typeClass+File.separator
        val sql = args.sql+File.separator+typeClass+File.separator

        val out = new File(sql+outputFileName)

        val hm = new HashMap[String, TypeData]

        for ( src <- srcs ) {
          val in = new File(txt+src)
          val itemType = src.split('.')(0).split('-')(2)

          parser.itemType = itemType

          logger.info("TXT input:  "+in.getPath)

          parser.parse(in).foreach( m => {
            if ( hm.contains(m.typeName) ) {
              if ( m.armor ) hm(m.typeName).armor = true
              if ( m.shield ) hm(m.typeName).shield = true
              if ( m.weapon ) hm(m.typeName).weapon = true
            } else {
              hm(m.typeName) = m
            }
          })
        }

        val types = hm.values.toList

        val printer = new MaterialTypePrinter

        debug(printer, types)

        val bw = new BufferedWriter(new FileWriter(out))

        def toSql(s: String): Unit = {
          bw.write(s)
          bw.newLine
        }

        printer.toSqlData(types, toSql)

        bw.close

        logger.info("Wrote: "+out.getPath)
      }

      def debug(printer: TypePrinter, types: List[TypeData]): Unit = {
        def toConsole(s: String): Unit = logger.info(s)

        printer.toTxtData(types, toConsole)

        logger.info("Found "+types.length+" unique types")
      }
    }
  }
}
