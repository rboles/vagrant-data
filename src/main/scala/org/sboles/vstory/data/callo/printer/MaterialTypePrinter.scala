package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.TypeData
    import org.sboles.vstory.data.callo.datatype.MaterialTypeData

    /**
     * Provides a printer for material types
     *
     * @author sboles
     */
    class MaterialTypePrinter extends TypePrinter {
      val _logger = Logger.getLogger(classOf[MaterialTypePrinter])

      override def typeTableName: String = "material_type"

      /**
       * Sorts list elements by strength property
       * @param l List of types
       * @return List of types sorted by strength
       */
      def strengthSort(l: List[TypeData]): List[TypeData] = {
        l.sortWith((e1, e2) => MaterialTypeData.strengthMap(e1.typeName) < MaterialTypeData.strengthMap(e2.typeName))
      }

      override def toTxtData(l: List[TypeData], write: (String) => Unit): Unit = {
        val nameMax = maxNameLength(l) + 1
        
        val nameFormat     = " %-"+nameMax+"s |"
        val atomicFormat   = " %-7s |"
        val strengthFormat = " %-9s |"
        val armorFormat  = " %-6s |"
        val weaponFormat = " %-7s |"
        val shieldFormat = " %-7s |"

        def boolToString(v: Boolean, format: String): String = {
          v match {
            case true  => String.format(format, "T")
            case false => String.format(format, "F")
          }
        }

        def strengthToString(v: TypeData): String = {
          String.format(strengthFormat,
                        MaterialTypeData.strength(v).toString)
        }

        def sprintf(v: TypeData): String = {
          String.format(nameFormat, v.typeName) +
          boolToString(v.atomic, atomicFormat) +
          strengthToString(v) +
          boolToString(v.armor, armorFormat) +
          boolToString(v.weapon, weaponFormat) +
          boolToString(v.shield, shieldFormat)
        }

        write(String.format(nameFormat, "# Name") +
          String.format(atomicFormat, "Atomic") +
          String.format(strengthFormat, "Strength") +
          String.format(armorFormat, "Armor") +
          String.format(weaponFormat, "Weapon") +
          String.format(shieldFormat, "Shield"))

        strengthSort(l).foreach({ v => write(sprintf(v)) })
      }

      override def toSqlData(l: List[TypeData], write: (String) => Unit): Unit = {
        def toBoolean(v: Boolean): String = {
          v match {
            case true => "T"
            case false => "F"
          }
        }
        def toShort(v: String): String = {
          v.substring(0,4) match {
            case "Weak" => "-"+v.split(' ')(1).substring(0,1)
            case "Stro" => "+"+v.split(' ')(1).substring(0,1)
            case _ => v.substring(0,1)
          }
        }
        def toStrength(v: String): String = {
          MaterialTypeData.strengthMap(v).toString
        }
        def sprintf(v: TypeData): String = {
          val sqlFormat = "insert into "+typeTableName+
          " (name, short, atomic, strength, armor, weapon, shield) values"+
          " ('%s', '%s', '%s', '%s', '%s', '%s', '%s');"
          String.format(sqlFormat,
                        v.typeName,
                        toShort(v.typeName),
                        toBoolean(v.atomic),
                        toStrength(v.typeName),
                        toBoolean(v.armor),
                        toBoolean(v.weapon),
                        toBoolean(v.shield)
                      )
        }

        write("delete from "+typeTableName+";")

        write("alter table "+typeTableName+" AUTO_INCREMENT=1;")

        strengthSort(l).foreach({ v => write(sprintf(v)) })
      }
    }
  }
}
