
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.sboles.vstory.data.callo.datatype.ComboData

    /**
     * Provides a material combo printer
     *
     * @author sboles
     */
    class MaterialComboPrinter extends ComboPrinter {

      var _itemType: String = _

      def this(itemType: String) = {
        this
        _itemType = itemType
      }

      override def comboTableName: String = {
        _itemType match {
          case "armor" => "material_combo_armor"
          case "weapon" => "material_combo_weapon"
        }
      }

      def itemTableName = {
        _itemType match {
          case "armor" => "armor_type"
          case "weapon" => "weapon_type"
        }
      }

      def typeTableName = "material_type"

      def t1MaxLength(combos: List[ComboData]): Int = {
        var max = 0

        def compare(v: ComboData): Unit =
          if ( v.t1.length > max ) max = v.t1.length

        combos.foreach(compare(_))

        max
      }

      def t2MaxLength(combos: List[ComboData]): Int = {
        var max = 0

        def compare(v: ComboData): Unit =
          if ( v.t2.length > max ) max = v.t2.length

        combos.foreach(compare(_))

        max
      }

      override def toSql(combos: List[ComboData], write: (String) => Unit): Unit = {
        val sqlFormat = "insert into "+comboTableName+
        " (material_1, material_2, object_type_1, object_type_2, result) values"+
        " ((select id from "+typeTableName+" where name = '%s'),"+
        " (select id from "+typeTableName+" where name = '%s'),"+
        " (select id from "+itemTableName+" where name = '%s'),"+
        " (select id from "+itemTableName+" where name = '%s'),"+
        " (select id from "+typeTableName+" where name = '%s')"+
        ");"

        def sprintf(c1: String, c2: String, t1: String, t2: String, r: String): String = {
          String.format(sqlFormat,
                        sqlSanitize(c1),
                        sqlSanitize(c2),
                        sqlSanitize(t1),
                        sqlSanitize(t2),
                        sqlSanitize(r))
        }
        
        def printCombo(c: ComboData): Unit = {
          write(sprintf(c.c1, c.c2, c.t1, c.t2, c.r1))
        }

        write("delete from "+comboTableName+";")

        write("alter table "+comboTableName+" AUTO_INCREMENT=1;")

        combos.foreach(printCombo(_))
      }

      /**
       * Prints material combos in a pipe delimited format
       * @param combos List of combos
       * @param write Function that outputs combos
       */
      override def toTxt(combos: List[ComboData], write: (String) => Unit): Unit = {
        val c1Max = c1MaxLength(combos) + 1
        val c2Max = c2MaxLength(combos) + 1
        val t1Max = t1MaxLength(combos) + 1
        val t2Max = t2MaxLength(combos) + 1

        val c1Format = " %-"+c1Max+"s |"
        val c2Format = " %-"+c2Max+"s |"
        val t1Format = " %-"+t1Max+"s |"
        val t2Format = " %-"+t2Max+"s | "

        def sprintf(v: ComboData): String = {
          val c1 = String.format(c1Format, v.c1)
          val c2 = String.format(c2Format, v.c2)
          val t1 = String.format(t1Format, v.t1)
          val t2 = String.format(t2Format, v.t2)
          c1 + c2 + t1 + t2 + v.r1
        }

        combos.foreach({ v => write(sprintf(v)) })
      }
    }
  }
}
