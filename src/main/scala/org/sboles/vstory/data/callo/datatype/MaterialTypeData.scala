
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.datatype {

    /**
     * Provides material type name to abbreviation mapping
     */
    object MaterialTypeData {
      val nameMap = Map("W"  -> "Wood",            "w"  -> "Wood",
                        "L"  -> "Leather",         "l"  -> "Leather",
                        "B"  -> "Bronze",          "b"  -> "Bronze",
                        "I"  -> "Iron",            "i"  -> "Iron",
                        "H"  -> "Hagane",          "h"  -> "Hagane",
                        "S"  -> "Silver",          "s"  -> "Silver",
                        "D"  -> "Damascus",        "d"  -> "Damascus")

      val strengthMap = Map("Wood"            -> 1,
                            "Leather"         -> 2,
                            "Bronze"          -> 3,
                            "Weak Iron"       -> 4,
                            "Iron"            -> 5,
                            "Weak Hagane"     -> 6,
                            "Hagane"          -> 7,
                            "Strong Hagane"   -> 8,
                            "Silver"          -> 9,
                            "Strong Silver"   -> 10,
                            "Damascus"        -> 11,
                            "Strong Damascus" -> 12)

      def strength(v: String): Int = strengthMap(v)

      def strength(v: TypeData): Int = strengthMap(v.typeName)
    }

    /**
     * Provides special handling for material types
     *
     * Material types are expressed as abbreviations in the combo file.
     *
     * @author sboles
     */
    class MaterialTypeData extends TypeData {

      import MaterialTypeData.nameMap

      def this(n: String) = {
        this
        typeName = n
      }

      def this(n: String, a: Boolean) = {
        this(n)
        atomic = a
      }

      override def typeName_=(v: String): Unit = {
        _typeName = v.substring(0,1) match {
          case "*" => v.substring(1,2) match {
            case "+" => {
              atomic = false
              "Strong " + nameMap(v.substring(2))
            }
            case "-" => {
              atomic = false
              "Weak " + nameMap(v.substring(2))
            }
            case _ => nameMap(v.substring(1))
          }
          case "+" => {
            atomic = false
            "Strong " + nameMap(v.substring(1))
          }
          case "-" => {
            atomic = false
            "Weak " + nameMap(v.substring(1))
          }
          case _ => nameMap(v)
        }
      }
    }
  }
}
