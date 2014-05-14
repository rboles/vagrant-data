
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.datatype {

    /**
     * Provides a container for data describing item type
     *
     * @author sboles
     */
    class TypeData {

      var _typeName: String = _

      var atomic: Boolean = true

      var armor: Boolean = false
      var shield: Boolean = false
      var weapon: Boolean = false

      def this(n: String) = {
        this
        typeName = n
      }

      def this(n: String, a: Boolean) = {
        this(n)
        atomic = a
      }

      /**
       * Sets the type name. The type name is massaged if it is prefixed with
       * "*", "+" or "-"
       * @param v Type name
       */
      def typeName_=(v: String): Unit = {
        _typeName = v.substring(0,1) match {
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
      * @return Type name
      */
      def typeName: String = _typeName

      /**
       */
      def setItemType(v: String): Unit = {
        v.toLowerCase match {
          case "armor" => armor = true
          case "shield" => shield = true
          case "weapon" => weapon = true
        }
      }

      /**
      * @return Pipe delimited string describing type
      */
      override def toString: String = {
        typeName + " | " + atomic
      }
    }
  }
}
