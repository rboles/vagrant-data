
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import scala.collection.mutable.HashMap

    import org.sboles.vstory.data.callo.datatype.ItemData
    import org.sboles.vstory.data.callo.datatype.TypeData

    trait Printer {

      /**
       * Sort a list of string alphabetically
       * @param l List of strings
       * @return Sorted list
       */
      def alphaSort(l: List[String]): List[String] = {
        l.sortWith((e1, e2) => (e1 compareTo e2) < 0)
      }

      /**
       * Sort a list of item data elements by item name
       * @param l List of items
       * @return Sorted list of items
       */
      def itemDataSort(l: List[ItemData]): List[ItemData] = {
        l.sortWith((e1, e2) => (e1.itemName compareTo e2.itemName) < 0)
      }

      /**
       * Sort a list of type data elements by type name
       * @param l List of types
       * @return Sorted list of types
       */
      def typeDataSort(l: List[TypeData]): List[TypeData] = {
        l.sortWith((e1, e2) => (e1.typeName compareTo e2.typeName) < 0)
      }

      /**
       * Sorts a list of items by item type. The return is a hash where
       * the keys are item type names and the values are lists of item data
       * instances. For some extra sugar the item data elements are sorted
       * alphabetically.
       * @param itemList List of items to sort
       * @return HashMap: (typeName, itemData)
       */
      def itemTypeSort(itemList: List[ItemData]): HashMap[String, List[ItemData]] = {
        val hm: HashMap[String, List[ItemData]] = new HashMap

        for ( item <- itemList ) {
          hm.contains(item.itemType) match {
            case true => {
              hm(item.itemType) = item :: hm(item.itemType)
            }
            case false => {
              hm(item.itemType) = List(item)
            }
          }
        }

        for ( k <- hm.keys ) hm(k) = itemDataSort(hm(k))

        hm
      }

      /**
       * Make string SQL safe
       * @param v
       * @return SQL safe string
       */
      def sqlSanitize(v: String) = v.trim.replace("'", "\\\'")
    }
  }
}
