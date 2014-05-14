
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.parser {

    import org.apache.log4j.Logger

    /**
     * Provides basic functionality for text data parsers
     */
    trait Parser {

      /**
       * Case classes for parse mode options
       */
      trait ParseMode { }
      case object GetTypes extends ParseMode
      case object GetCombos extends ParseMode

      /**
       * Implementors must provide a logger
       */
      def logger: Logger

      /**
       * Returns the values in a combo line. The base implementation of this
       * method splits on pipe and returns an list of trimmed values
       * @param line Line of data
      * @return List of values
      */
      def getComboValues(line: String): List[String] = {
        line.split('|').map(v => toValue(v.trim)).toList
      }

      /**
      * Massages a value read from a combo file.
      * @param s Item value from combo file
      * @return Massaged item value
      */
      def toValue(s: String): String = {
        s.substring(0,1) match {
          case "*" => s.substring(1,2) match {
            case "+" => "Strong "+s.substring(2)
            case "-" => "Weak "+s.substring(2)
            case _ => s.substring(1)
          }
          case "+" => "Strong "+s.substring(1)
          case "-" => "Weak "+s.substring(1)
          case _ => s
        }
      }

      /**
      * Returns true if the line stars with "#"
      * @param line Line of data
      * @return True if the line is a comment
      */
      def isComment(line: String): Boolean = {
        line != null && line.length > 0 &&
        (line.substring(0,1) match {
          case "#" => true
          case _ => false
        })
      }

      /**
      * Returns true if the line is prefixed with "--". This notation is used
      * to identify lines that contain data type information.
      * @param line Line of data
      * @return True if the line is a type identifier
      */
      def isTypeIdentifier(line: String): Boolean = {
        line != null && line.length > 1 && line.substring(0,2) == "--"
      }

      /**
      * Parses types out of a line of data containing type identifiers.
      * Lines prefixed with "--" are assumed to contain type identifiers.
      * @param line Line of data
      * @return List containing type 1, type 2
      */
      def getTypes(line: String): List[String] = {
        line.substring(2).split('+').map(v => v.trim).toList
      }

      /**
      * Returns true if the line looks like it contains combination results.
      * The base implementation just looks for a pipe character in the line.
      * @param line Line of data
      * @return True if the line looks like a combo definition
      */
      def isComboResultLine(line: String): Boolean = {
        line != null && line.contains("|")
      }

      /**
      * Tests the line to see if it looks like it can be processed for values
      * The base implementation looks for a line that looks like a combo
      * result line.
      * @param line Line of data
      * @return True if the line looks like it can be processed for valules
      */
      def canProcess(line: String): Boolean = {
        line != null && line.length > 0 &&
        !isComment(line) && !isTypeIdentifier(line) &&
        isComboResultLine(line)
      }

      /**
      * Splits a line on pipes and returns a list of the elements in the line
      * @param line Line of data
      * @return List of items in the line
      */
      def getComboItems(line: String): List[String] = {
        line.split('|').map(v => v.trim).toList
      }
    }
  }
}
