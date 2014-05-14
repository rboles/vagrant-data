
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.parser {

    import scala.collection.mutable.HashMap

    import java.io.{BufferedReader,File,FileReader}

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.ItemData

    /**
     * Parses combo data files for unique items
     */
    class ItemParser extends Parser {

      private val _logger = Logger.getLogger(classOf[ItemParser])

      override def logger: Logger = _logger

      /**
       * Lines that are not blank or comments are processed
       * @param line Line of text data
       * @return True if line is not blank or a comment
       */
      override def canProcess(line: String): Boolean = {
        line != null && line.length > 0 && !isComment(line)
      }

      /**
       * Parses the combo file for unique items
       * @param in Combo file
       * @return List of unique items
       */
      def parse(in: File): List[ItemData] = {
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
       * Parses the combo data file for unique items
       * @param br Buffered reader on combo data file
       * @return List of unique items
       */
      def parse(br: BufferedReader): List[ItemData] = {
        var parseMode: ParseMode = GetTypes
        var itemTypes: List[String] = List()

        val itemMap = new HashMap[String, ItemData]
        val resultMap = new HashMap[String, ItemData]

        var l = br.readLine

        while ( l != null ) {
          if ( canProcess(l) ) {

            if ( isTypeIdentifier(l) ) parseMode = GetTypes

            parseMode match {
              case GetTypes => {
                if ( isTypeIdentifier(l) ) {
                  itemTypes = getTypes(l)
                  parseMode = GetCombos
                }
              }
              case GetCombos => {
                if ( isComboResultLine(l) ) {
                  val items = getComboItems(l)
                  val i1 = new ItemData(items(0), itemTypes(0))
                  val i2 = new ItemData(items(1), itemTypes(1))
                  val r1 = new ItemData(items(2))
                  itemMap(i1.itemName) = i1
                  itemMap(i2.itemName) = i2
                  resultMap(r1.itemName) = r1
                  if ( items.length > 3 ) {
                    val r2 = new ItemData(items(3))
                    resultMap(r2.itemName) = r2
                  }
                }
              }
            }
          }

          l = br.readLine
        }

        br.close

        (itemMap ++ verifyTypes(itemMap, resultMap)).values.toList
      }

      /**
      * Verifies that all items in HashMap, hm2, have item types by checking
      * against the items in HashMap, hm1. It is possible that a combination
      * result is non-atomic and will have a "Strong" or "Weak" prefix. In
      * this case the match is made on the string after the prefix.
      * @param hm1
      * @param hm2
      * @return HashMap, hm2
      */
      def verifyTypes(hm1: HashMap[String, ItemData],
                      hm2: HashMap[String, ItemData]): HashMap[String, ItemData] = {
        for ( k <- hm2.keys ) {
          hm1.contains(k) match {
            case true => hm2(k).itemType = hm1(k).itemType
            case false => {
              k.substring(0,4) match {
                case "Weak" => {
                  hm1.contains(k.substring(5)) match {
                    case true => hm2(k).itemType = hm1(k.substring(5)).itemType
                    case false => logger.error("Failed to find a match for"+
                                               " result combination: "+k)
                  }
                }
                case "Stro" => {
                  hm1.contains(k.substring(7)) match {
                    case true => hm2(k).itemType = hm1(k.substring(7)).itemType
                    case false => logger.error("Failed to find a match for"+
                                               " result combination: "+k)
                  }
                }
                case _ => {
                  logger.error("Failed to find a match for"+
                               " result combination: "+k)
                }
              }
            }
          }
        }

        hm2
      }
    }
  }
}
