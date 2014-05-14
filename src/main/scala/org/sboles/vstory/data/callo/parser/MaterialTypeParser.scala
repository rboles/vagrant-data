
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.parser {

    import scala.collection.mutable.HashMap

    import java.io.{BufferedReader,File,FileReader}

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.MaterialTypeData
    import org.sboles.vstory.data.callo.datatype.TypeData
    import org.sboles.vstory.data.callo.datatype.ItemData

    /**
     * Parses combo data files for unique material types
     */
    class MaterialTypeParser extends TypeParser {

      // "armor" or "weapon"
      var itemType = "unknown"

      private val _logger = Logger.getLogger(classOf[MaterialTypeParser])

      case object GetComboItems extends ParseMode
      case object GetSlotOneTypes extends ParseMode
      case object GetSlotTwoTypeAndResult extends ParseMode

      /**
       * @param _itemType "armor", or "weapon"
       */
      def this(_itemType: String) {
        this
        itemType = _itemType
      }

      override def logger: Logger = _logger

      /**
       * Returns true if the line looks like a combination result. In the
       * case of material combos, this means the third character is a pipe.
       * @param line Line of data
       * @return True if the line looks like a combo definition
       */
      override def isComboResultLine(line: String): Boolean = {
        line != null && line.length > 3 && line.substring(3,4) == "|"
      }

      /**
       * Searches combination data for unique material types.
       *
       * @param br Buffered reader to combination data
       * @return List of type data
       */
      override def parse(br: BufferedReader): List[TypeData] = {
        var parseMode: ParseMode = GetComboItems
        var hm = new HashMap[String, TypeData]

        // armor or weapon
        val fileItemType = itemType

        var slotOneItem = new ItemData
        var slotTwoItem = new ItemData

        var l = br.readLine

        while ( l != null ) {
          if ( canProcess(l) ) {

            if ( isTypeIdentifier(l) ) parseMode = GetComboItems

            parseMode match {
              case GetComboItems => {
                val itemTypes = getTypes(l)
                slotOneItem = new ItemData(itemTypes(0))
                slotTwoItem = new ItemData(itemTypes(1))

                parseMode = GetSlotOneTypes
              }
              case GetSlotOneTypes => {
                val itemType = {
                  slotOneItem.itemName.toLowerCase match {
                    case "shield" => "shield"
                    case _ => fileItemType
                  }
                }
                l.trim.split("\\s+").foreach( v => {
                  val m = new MaterialTypeData(v)
                  m.setItemType(itemType)
                  // m.itemType = itemType
                  if ( !hm.contains(m.typeName) ) hm(m.typeName) = m
                  hm(m.typeName).setItemType(itemType)
                } )
                parseMode = GetSlotTwoTypeAndResult
              }
              case GetSlotTwoTypeAndResult => {
                val itemType = {
                  slotTwoItem.itemName.toLowerCase match {
                    case "shield" => "shield"
                    case _ => fileItemType
                  }
                }
                val vv = l.trim.split('|')
                val v = vv(0).trim

                val m = new MaterialTypeData(v)
                if ( !hm.contains(m.typeName) ) hm(m.typeName) = m
                hm(m.typeName).setItemType(itemType)

                vv(1).trim.split("\\s+").foreach( v => {
                  val m = new MaterialTypeData(v)
                  if ( !hm.contains(m.typeName) ) hm(m.typeName) = m
                  hm(m.typeName).setItemType(itemType)
                } )
              }
            }
          }

          l = br.readLine
        }

        br.close

        hm.values.toList
      }
    }
  }
}
