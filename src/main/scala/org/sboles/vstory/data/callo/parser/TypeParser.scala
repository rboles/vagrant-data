
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.parser {

    import scala.collection.mutable.HashMap

    import java.io.{BufferedReader,File,FileReader}

    import org.apache.log4j.Logger

    import org.sboles.vstory.data.callo.datatype.TypeData

    /**
     * Parses combo data files for unique types
     */
    class TypeParser extends Parser {

      private val _logger = Logger.getLogger(classOf[TypeParser])

      override def logger: Logger = _logger

      /**
       * Lines that are not blank or comments are processed
       * @param line Line of text data
       * @return True if line is not blank or a comment
       */
      override def canProcess(line: String): Boolean = {
        line != null && line.length > 0 && !isComment(line)
      }

      /**
       * Parses the combo file for unique object types
       * @param in Combo file
       * @return List of unique object types
       */
      def parse(in: File): List[TypeData] = {
        val reader = try {
          Option(new BufferedReader(new FileReader(in)))
        } catch {
          case e => {
            None
          }
        }

        reader match {
          case Some(br) => parse(br)
          case None => {
            logger.error("Failed to read from data source: "+in)
            List()
          }
        }
      }

      /**
       * Parses the combo data file for unique object types
       * @param br Buffered reader on combo data file
       * @return List of unique object types
       */
      def parse(br: BufferedReader): List[TypeData] = {
        var l = br.readLine
        val hm = new HashMap[String, TypeData]

        while ( l != null ) {
          if ( canProcess(l) && isTypeIdentifier(l) ) {
            for ( t <- getTypes(l) ) {
              val v = new TypeData(t)
              hm(v.typeName) = v
            }
          }
          l = br.readLine
        }

        br.close

        hm.values.toList
      }
    }
  }
}
