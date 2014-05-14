
package org.sboles.vstory.data.callo {

  package org.sboles.vstory.data.callo.worker {
    
    import org.apache.log4j.Logger

    trait Worker {

      protected var _objectClass:Option[String] = None

      /**
       * Implementors must provide a logger
       */
      def logger: Logger

      /**
       * Implementors must provide a work method
       */
      def work(args: RunTimeArgs): Unit 

      /**
       * Returns the value of the mode argument
       * @param args Run time arguments
       * @return String value of mode argument
       */
      def mode(args: RunTimeArgs): String = {
        args.mode match {
          case Some(v) => v
          case _ => {
            logger.error("Failed to find value for mode argument")
            ""
          }
        }
      }

      /**
       * Returns the object class: armor, material, weapon; based on the
       * valueo of the mode argument
       * @param args Run time arguments
       * @return Object class
       */
      def objectClass(args: RunTimeArgs): String = {
        _objectClass match {
          case Some(v) => v
          case None => mode(args).split('-')(0)
        }
      }
    }
  }
}
