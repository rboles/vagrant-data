package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    import org.apache.log4j.Logger

    /**
     * Provides a printer for weapon types
     *
     * @author sboles
     */
    class WeaponTypePrinter extends TypePrinter {

      val _logger = Logger.getLogger(classOf[WeaponTypePrinter])

      override def typeTableName: String = "weapon_type"
    }
  }
}
