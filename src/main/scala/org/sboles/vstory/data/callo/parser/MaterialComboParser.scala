
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.parser {

    import scala.collection.mutable.Queue

    import java.io.{BufferedReader,File,FileReader}

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.ComboData
    import org.sboles.vstory.data.callo.datatype.ItemData

    import org.sboles.vstory.data.callo.datatype.MaterialTypeData

    /**
     * Parses combo data files for unique items
     */
    class MaterialComboParser extends ComboParser {

      private val _logger = Logger.getLogger(classOf[MaterialComboParser])

      override def logger: Logger = _logger

      override def canProcess(line: String): Boolean = {
        line != null && line.length > 0 && !isComment(line)
      }

      case object GetSlotOneTypes extends ParseMode
      case object GetSlotTwoTypeAndResult extends ParseMode

      /**
       * Parses the material combo data file for combos
       * @param br Buffered reader on combo data file
       * @return List of combos
       */
      override def parse(br: BufferedReader): List[ComboData] = {
        val combos = new Queue[ComboData]

        var parseMode: ParseMode = GetTypes

        var slotOneItem = new ItemData
        var slotTwoItem = new ItemData
        var slotOneMaterials: Array[MaterialTypeData] = Array()
        var slotTwoMaterial = new MaterialTypeData
        var comboResults: Array[MaterialTypeData] = Array()

        var l = br.readLine

        while ( l != null ) {
          if ( canProcess(l) ) {
            if ( isTypeIdentifier(l) ) parseMode = GetTypes

            parseMode match {
              case GetTypes => {
                val vv = getTypes(l)
                slotOneItem = new ItemData(vv(0))
                slotTwoItem = new ItemData(vv(1))
                parseMode = GetSlotOneTypes
              }
              case GetSlotOneTypes => {
                slotOneMaterials = l.trim.split("\\s+").map(new MaterialTypeData(_))
                parseMode = GetSlotTwoTypeAndResult
              }
              case GetSlotTwoTypeAndResult => {
                val vv = l.trim.split('|')
                slotTwoMaterial = new MaterialTypeData(vv(0).trim)
                comboResults = vv(1).trim.split("\\s+").map(new MaterialTypeData(_))

                for ( x <- 0 until slotOneMaterials.length ) {
                  val combo = new ComboData(slotOneMaterials(x).typeName,
                                            slotTwoMaterial.typeName,
                                            comboResults(x).typeName)
                  combo.t1 = slotOneItem
                  combo.t2 = slotTwoItem

                  combos += combo
                }
              }
            }
          }

          l = br.readLine
        }

        combos.toList
      }
    }
  }
}
