
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.datatype {

    /**
     * Provides a container for data describing item combos
     *
     * @author sboles
     */
    class ComboData {

      // combo item 1
      var _c1: String = _

      // combo item 2
      var _c2: String = _

      // combo item 1 type
      var _t1: String = _

      // combo item 2 type
      var _t2: String = _

      // result 1
      var _r1: String = _

      // result if items 1 & 2 are swapped
      var _r2: Option[String] = None

      /**
       * @param c1 Combinant 1
       * @param c2 Combinant 2
       * @param r1 Result 1
       */
      def this(c1: String, c2: String, r1: String) = {
        this
        _c1 = c1
        _c2 = c2
        _r1 = r1
      }

      /**
       * @param c1 Combinant 1
       * @param c2 Combinant 2
       * @param r1 Result 1
       */
      def this(c1: ItemData, c2: ItemData, r1: ItemData) = {
        this(c1.itemName, c2.itemName, r1.itemName)
      }

      /**
       * @param c1 Combinant 1
       * @param c2 Combinant 2
       * @param r1 Result 1
       */
      def this(c1: TypeData, c2: TypeData, r1: TypeData) = {
        this(c1.typeName, c2.typeName, r1.typeName)
      }

      /**
       * @param c1 Combinant 1
       * @param c2 Combinant 2
       * @param r1 Result 1
       * @param r2 Result 2
       */
      def this(c1: String, c2: String, r1: String, r2: String) = {
        this(c1, c2, r1)
        _r2 = Some(r2)
      }

      /**
       * @param c1 Combinant 1
       * @param c2 Combinant 2
       * @param r1 Result 1
       * @param r2 Result 2
       */
      def this(c1: ItemData, c2: ItemData, r1: ItemData, r2: ItemData) = {
        this(c1.itemName, c2.itemName, r1.itemName, r2.itemName)
      }

      /**
       * @param c1 Combinant 1
       * @param c2 Combinant 2
       * @param r1 Result 1
       * @param r2 Result 2
       */
      def this(c1: TypeData, c2: TypeData, r1: TypeData, r2: TypeData) = {
        this(c1.typeName, c2.typeName, r1.typeName, r2.typeName)
      }

      /**
       * @return c1 Combinant 1
       */
      def c1: String = _c1

      /**
       * @return c2 Combinant 2
       */
      def c2: String = _c2

      /**
       * @return r1 Result 1
       */
      def r1: String = _r1

      /**
       * @return r2 Optional result 2
       */
      def r2: Option[String] = _r2

      /**
       * @return Object 1 type
       */
      def t1: String = _t1

      /**
       * @param v Type of object 1
       */
      def t1_=(v: String): Unit = {
        _t1 = v
      }

      /**
       * @param v Type of object 1
       */
      def t1_=(v: TypeData): Unit = {
        _t1 = v.typeName
      }

      /**
       * @param v Type of object 1
       */
      def t1_=(v: ItemData): Unit = {
        _t1 = v.itemName
      }

      /**
       * @param v Type of object 2
       */
      def t2_=(v: String): Unit = {
        _t2 = v
      }

      /**
       * @param v Type of object 2
       */
      def t2_=(v: TypeData): Unit = {
        _t2 = v.typeName
      }

      /**
       * @param v Type of object 2
       */
      def t2_=(v: ItemData): Unit = {
        _t2 = v.itemName
      }

      /**
       * @return Object 2 type
       */
      def t2: String = _t2
    }
  }
}
