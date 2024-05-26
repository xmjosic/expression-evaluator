package hr.xmjosic.expressionevaluator.util;

import static org.junit.jupiter.api.Assertions.*;

import hr.xmjosic.expressionevaluator.model.TokenType;
import org.junit.jupiter.api.Test;

class CastUtilTest {

  @Test
  void castToTokenType() {
    TokenType tokenType = TokenType.EQUAL_OPERATOR;
    TokenType result = CastUtil.castToTokenType(tokenType);
    assertEquals(tokenType, result);
  }

  @Test
  void castToTokenTypeWithException() {
    Object nonTokenType = new Object();
    assertThrows(IllegalArgumentException.class, () -> CastUtil.castToTokenType(nonTokenType));
  }

  @Test
  void castToPrimitiveBoolean() {
    Boolean bool = Boolean.TRUE;
    boolean result = CastUtil.castToPrimitiveBoolean(bool);
    assertTrue(result);
  }

  @Test
  void castToPrimitiveBooleanWithException() {
    Object nonTokenType = new Object();
    assertThrows(
        IllegalArgumentException.class, () -> CastUtil.castToPrimitiveBoolean(nonTokenType));
  }

  @Test
  void castToPrimitiveDouble() {
    Double num = 2.0;
    double result = CastUtil.castToPrimitiveDouble(num);
    assertEquals(num, result);
  }

  @Test
  void castToPrimitiveDoubleWithException() {
    Object nonTokenType = new Object();
    assertThrows(
        IllegalArgumentException.class, () -> CastUtil.castToPrimitiveDouble(nonTokenType));
  }
}
