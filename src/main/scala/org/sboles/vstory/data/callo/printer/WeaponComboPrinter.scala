
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.sboles.vstory.data.callo.datatype.ComboData

    /**
     * Provides base functionality for combo printers
     *
     * @author sboles
     */
    class WeaponComboPrinter extends ComboPrinter {

      override def comboTableName = "weapon_combo"

      def itemTableName = "weapon"

      override def toSql(combos: List[ComboData], write: (String) => Unit): Unit = {

        val sqlFormat = "insert into "+comboTableName+
        " (weapon_1, weapon_2, result) values"+
        " ((select id from "+itemTableName+" where name = '%s'),"+
        " (select id from "+itemTableName+" where name = '%s'),"+
        " (select id from "+itemTableName+" where name = '%s')"+
        ");"
        
        def sprintf(c1: String, c2: String, r: String): String = {
          String.format(sqlFormat,
                        sqlSanitize(c1),
                        sqlSanitize(c2),
                        sqlSanitize(r))
        }

        def printCombo(c: ComboData): Unit = {
          write(sprintf(c.c1, c.c2, c.r1))
          c.r2 match {
            case Some(r) => write(sprintf(c.c2, c.c1, r))
            case None => { }
          }
        }

        write("delete from "+comboTableName+";")

        write("alter table "+comboTableName+" AUTO_INCREMENT=1;")

        combos.foreach(printCombo(_))
      }
    }
  }
}
