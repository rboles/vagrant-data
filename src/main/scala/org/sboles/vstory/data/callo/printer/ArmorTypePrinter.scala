package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.apache.log4j.Logger

    /**
     * Provides a printer for armor types
     *
     * @author sboles
     */
    class ArmorTypePrinter extends TypePrinter {

      val _logger = Logger.getLogger(classOf[ArmorTypePrinter])

      override def typeTableName: String = "armor_type"
    }
  }
}
