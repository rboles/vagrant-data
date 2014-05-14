package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    /**
     * Provides a printer for weapon items
     *
     * @author sboles
     */
    class WeaponItemPrinter extends ItemPrinter {

      override def itemTableName: String = "weapon"
      
      override def typeTableName: String = "weapon_type"
    }
  }
}
