
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.sboles.vstory.data.callo.datatype.ComboData

    /**
     * Provides an armor combo printer
     *
     * @author sboles
     */
    class ArmorComboPrinter extends ComboPrinter {

      override def comboTableName = "armor_combo"

      def itemTableName = "armor"

      override def toSql(combos: List[ComboData], write: (String) => Unit): Unit = {

        val sqlFormat = "insert into "+comboTableName+
        " (armor_1, armor_2, result) values"+
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
