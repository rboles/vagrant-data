
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.parser {

    import scala.collection.mutable.Queue

    import java.io.{BufferedReader,File,FileReader}

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.ComboData

    import org.sboles.vstory.data.callo.datatype.ItemData

    /**
     * Parses combo data files for unique items
     */
    class ComboParser extends Parser {

      private val _logger = Logger.getLogger(classOf[ComboParser])

      override def logger: Logger = _logger

      override def canProcess(line: String): Boolean = {
        line != null && line.length > 0 &&
        !isComment(line) &&
        !isTypeIdentifier(line) &&
        isComboResultLine(line)
      }

      /**
       * Parses the combo file for combos
       * @param in Combo file
       * @return List combos
       */
      def parse(in: File): List[ComboData] = {
        val reader = try {
          Option(new BufferedReader(new FileReader(in)))
        } catch {
          case e => {
            None
          }
        }

        reader match {
          case Some(br) => parse(br)
          case None => {
            logger.error("Failed to read from data source: "+in)
            List()
          }
        }
      }

      /**
       * Returns a new instance of ComboData constructed from the
       * elements in the item list
       * @param l List of item names
       * @return New instance of ComboData
       */
      def comboData(l: Array[String]): ComboData = {
        l.length match {
          case 3 => {
            new ComboData((new ItemData(l(0))).itemName,
                          (new ItemData(l(1))).itemName,
                          (new ItemData(l(2))).itemName)
          }
          case 4 => {
            new ComboData((new ItemData(l(0))).itemName,
                          (new ItemData(l(1))).itemName,
                          (new ItemData(l(2))).itemName,
                          (new ItemData(l(3))).itemName)
          }
        }
      }

      /**
       * Parses the combo data file for combos
       * @param br Buffered reader on combo data file
       * @return List of combos
       */
      def parse(br: BufferedReader): List[ComboData] = {
        val combos = new Queue[ComboData]

        var l = br.readLine

        while ( l != null ) {
          if ( canProcess(l) ) {
            combos += comboData(l.split('|').map(_.trim))
          }

          l = br.readLine
        }

        combos.toList
      }
    }
  }
}
