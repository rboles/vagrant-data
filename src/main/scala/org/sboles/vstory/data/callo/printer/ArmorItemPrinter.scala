package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.printer {

    /**
     * Provides a printer for armor items
     *
     * @author sboles
     */
    class ArmorItemPrinter extends ItemPrinter {

      override def itemTableName: String = "armor"
      
      override def typeTableName: String = "armor_type"
    }
  }
}
