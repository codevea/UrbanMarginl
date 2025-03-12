package outils.son.exceptions;

import java.io.Serializable;

/**
 * Exception levée si le type de son n'est pas reconnu <br>
 */

public class SonTypeException
    extends SonException implements Serializable
{
  /**
   * Construit l'exception
   */
  public SonTypeException()
  {
    super("Le type du son n'est pas reconnu");
  }

}
