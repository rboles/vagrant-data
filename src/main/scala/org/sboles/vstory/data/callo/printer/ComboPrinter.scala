
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.sboles.vstory.data.callo.datatype.ComboData

    import scala.collection.mutable.HashMap

    /**
     * Provides base functionality for combo printers
     *
     * @author sboles
     */
    trait ComboPrinter extends Printer {

      def comboTableName: String

      def c1MaxLength(combos: List[ComboData]): Int = {
        var max = 0

        def compare(v: ComboData): Unit =
          if ( v.c1.length > max ) max = v.c1.length

        combos.foreach(compare(_))

        max
      }

      def c2MaxLength(combos: List[ComboData]): Int = {
        var max = 0

        def compare(v: ComboData): Unit =
          if ( v.c2.length > max ) max = v.c2.length

        combos.foreach(compare(_))
        
        max
      }

      def r1MaxLength(combos: List[ComboData]): Int = {
        var max = 0

        def compare(v: ComboData): Unit =
          if ( v.r1.length > max ) max = v.r1.length

        combos.foreach(compare(_))
        
        max
      }

      def r2MaxLength(combos: List[ComboData]): Int = {
        var max = 0

        def compare(v: ComboData): Unit = {
          v.r2 match {
            case Some(r) => if ( r.length > max ) max = r.length
            case None => { }
          }
        }

        combos.foreach(compare(_))

        max
      }

      /**
       * Prints combos in a pipe delimited format
       * @param combos List of combos
       * @param write Function that outputs combos
       */
      def toTxt(combos: List[ComboData], write: (String) => Unit): Unit = {
        val c1Max = c1MaxLength(combos) + 1
        val c2Max = c2MaxLength(combos) + 1
        val r1Max = r1MaxLength(combos) + 1
        val r2Max = r2MaxLength(combos) + 1

        val c1Format = " %-"+c1Max+"s |"
        val c2Format = " %-"+c2Max+"s |"
        val r1Format = " %-"+r1Max+"s | "

        def sprintf(v: ComboData): String = {
          val c1 = String.format(c1Format, v.c1)
          val c2 = String.format(c2Format, v.c2)
          val r1 = String.format(c1Format, v.r1)
          val r2 = v.r2 match {
            case Some(r) => r
            case None => ""
          }
          c1 + c2 + r1 + r2
        }

        combos.foreach({ v => write(sprintf(v)) })
      }

      def toSql(combos: List[ComboData], write: (String) => Unit): Unit
    }
  }
}
