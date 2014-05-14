
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.datatype {

    /**
     * Provides a container for data describing an item
     *
     * @author sboles
     */
    class ItemData {

      var armor = false
      var shield = false
      var weapon = false

      var itemType: String = _

      var _itemName: String = _

      var _atomic: Boolean = true

      def this(n: String) = {
        this
        itemName = n
      }

      def this(n: String, t: String) = {
        this(n)
        itemType = t
      }

      def this(n: String, t: TypeData) = {
        this(n)
        itemType = t
      }

      def this(n: String, t: String, a: Boolean) = {
        this(n, t)
        atomic = a
      }

      def this(n: String, t: TypeData, a: Boolean) = {
        this(n, t)
        atomic = a
      }

      /**
       * Sets the item name. The item name is massaged if it is prefixed with
       * "*", "+" or "-"
       * @param v Item name
       */
      def itemName_=(v: String): Unit = {
        _itemName = v.substring(0,1) match {
          case "*" => v.substring(1,2) match {
            case "+" => {
              atomic = false
              "Strong "+v.substring(2)
            }
            case "-" => {
              atomic = false
              "Weak "+v.substring(2)
            }
            case _ => v.substring(1)
          }
          case "+" => {
            atomic = false
            "Strong "+v.substring(1)
          }
          case "-" => {
            atomic = false
            "Weak "+v.substring(1)
          }
          case _ => v
        }
      }

      /**
       * @return Item name
       */
      def itemName: String = _itemName

      /**
       * @param Item type
       */
      def itemType_=(v: TypeData): Unit = {
        itemType = v.typeName
      }

      /**
      * @param True if item is atomic; false if not
      */
      def atomic_=(v: Boolean): Unit = {
        _atomic = v
      }

      /**
      * @return True if item is atomic; false if not
      */
      def atomic: Boolean = _atomic

    }
  }
}
