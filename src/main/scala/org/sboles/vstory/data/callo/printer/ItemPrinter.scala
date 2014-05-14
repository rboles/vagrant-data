
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.sboles.vstory.data.callo.datatype.ItemData

    /**
     * Provides base functionality for item printers
     *
     * @author sboles
     */
    trait ItemPrinter extends Printer {

      def itemTableName: String

      def typeTableName: String

      /**
       * Searches the item data list for the length of the longest item
       * name and returns that length
       * @param itemList List of items
       * @return Length of longest item name
       */
      def maxNameLength(itemList: List[ItemData]): Int = {
        var max = 0

        def checkLength(v: ItemData): Unit =
          if ( v.itemName.length > max ) max = v.itemName.length

        itemList.foreach(checkLength(_))

        max
      }

      /**
       * Searches the item data list for the length of the longest item
       * type name and returns that length
       * @param itemList List of items
       * @return Length of longest item type name
       */
      def maxTypeLength(itemList: List[ItemData]): Int = {
        var max = 0

        def checkLength(v: ItemData): Unit = {
          if ( v.itemType.length > max ) max = v.itemType.length
        }

        itemList.foreach(checkLength(_))

        max
      }

      /**
       * Prints items as SQL insert statements
       * @param itemList List of items
       * @param write Function that outputs SQL statements
       */
      def toSqlData(itemList: List[ItemData], write: (String) => Unit): Unit = {
        val sqlFormat = "insert into "+itemTableName+
        " (name, type, atomic) values"+
        " ('%s', (select id from "+typeTableName+" where name = '%s'), '%s');"

        def sprintf(item: ItemData): String = {
          String.format(sqlFormat,
                        sqlSanitize(item.itemName),
                        sqlSanitize(item.itemType),
                        (item.atomic match {
                          case true => "T"
                          case false => "F"}))
        }

        write("delete from "+itemTableName+";")

        write("alter table "+itemTableName+" AUTO_INCREMENT=1;")

        val hm = itemTypeSort(itemList)
        val keys = alphaSort(hm.keys.toList)

        for ( k <- keys ) hm(k).foreach({ v => write(sprintf(v)) })
      }

      /**
       * Prints items in a pipe delimited format
       * @param itemList List of items
       * @param write Function that outputs items
       */
      def toTxtData(itemList: List[ItemData], write: (String) => Unit): Unit = {
        val nameMax = maxNameLength(itemList) + 1
        val typeMax = maxTypeLength(itemList) + 1

        val nameFormat = " %-"+nameMax+"s |"
        val typeFormat = " %-"+typeMax+"s |"

        def sprintf(v: ItemData): String = {
          val sName = String.format(nameFormat, v.itemName)
          val sType = String.format(typeFormat, v.itemType)
          sName + sType + ( v.atomic match {
            case true  => " T"
            case false => " F"
          })
        }

        val hm = itemTypeSort(itemList)
        val keys = alphaSort(hm.keys.toList)

        for ( k <- keys ) hm(k).foreach({ v => write(sprintf(v)) })
      }
    }
  }
}
