package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.sboles.vstory.data.callo.datatype.TypeData

    /**
     * Provides base functionality for type printers
     *
     * @author sboles
     */
    trait TypePrinter extends Printer {

      def typeTableName: String

      /**
       * Searches the type data list for the length of the longest type
       * name and returns that length
       * @param l List of types
       * @return Length of longest types name
       */
      def maxNameLength(l: List[TypeData]): Int = {
        var max = 0

        def checkLength(v: TypeData): Unit =
          if ( v.typeName.length > max ) max = v.typeName.length

        l.foreach(checkLength(_))

        max
      }


      /**
       * Prints types in a pipe delimited format
       * @param typeList List of types
       * @param write Function that outputs types
       */
      def toTxtData(l: List[TypeData], write: (String) => Unit): Unit = {
        val nameMax = maxNameLength(l) + 1

        val nameFormat = " %-"+nameMax+"s |"

        def sprintf(v: TypeData): String = {
          val sName = String.format(nameFormat, v.typeName)
          sName + ( v.atomic match {
            case true  => " T"
            case false => " F"
          })
        }

        typeDataSort(l).foreach({ v=> write(sprintf(v)) })
      }

      /**
       * Prints types as SQL insert statements
       * @param l List of types
       * @param write Function that outputs SQL statements
       */
      def toSqlData(l: List[TypeData], write: (String) => Unit): Unit = {
        def sprintf(v: TypeData): String = {
          val sqlFormat = "insert into "+typeTableName+
          " (name) values ('%s');"
          String.format(sqlFormat, sqlSanitize(v.typeName))
        }

        write("delete from "+typeTableName+";")

        write("alter table "+typeTableName+" AUTO_INCREMENT=1;")

        typeDataSort(l).foreach({ v => write(sprintf(v)) })
      }
    }
  }
}
