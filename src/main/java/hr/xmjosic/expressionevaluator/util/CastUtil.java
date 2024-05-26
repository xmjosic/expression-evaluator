package hr.xmjosic.expressionevaluator.util;

import hr.xmjosic.expressionevaluator.model.TokenType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** A utility class for casting objects to specific types. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CastUtil {

  /**
   * Casts the given operator object to a TokenType object.
   *
   * @param operator the operator object to be cast
   * @return the cast TokenType object
   * @throws IllegalArgumentException if the operator is not an instance of TokenType
   */
  public static TokenType castToTokenType(Object operator) {
    if (!(operator instanceof TokenType t)) throw new IllegalArgumentException("Operator invalid.");
    return t;
  }

  /**
   * Casts the given operand to a primitive boolean.
   *
   * @param operand the operand to be cast
   * @return the primitive boolean value of the operand
   * @throws IllegalArgumentException if the operand is not a boolean
   */
  public static boolean castToPrimitiveBoolean(Object operand) {
    if (!(operand instanceof Boolean b)) throw new IllegalArgumentException("Operand invalid.");
    return b;
  }

  /**
   * Casts the given operand to a primitive double.
   *
   * @param operand the operand to be cast
   * @return the primitive double value of the operand
   * @throws IllegalArgumentException if the operand is not a double
   */
  public static double castToPrimitiveDouble(Object operand) {
    if (!(operand instanceof Double d)) throw new IllegalArgumentException("Operand invalid.");
    return d;
  }
}
