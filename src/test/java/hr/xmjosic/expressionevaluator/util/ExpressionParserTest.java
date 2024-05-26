package hr.xmjosic.expressionevaluator.util;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.xmjosic.expressionevaluator.FileUtil;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ExpressionParserTest {

  private ExpressionParser parser;

  @BeforeEach
  void setUp() {
    parser = new ExpressionParser(new Tokenizer());
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource("provideExpressions")
  void evaluate(String expression, boolean expected) {
    JsonNode jsonNode =
        new ObjectMapper().readTree(FileUtil.readResourceFromClasspath("jsonInput.json"));
    boolean retVal = assertDoesNotThrow(() -> parser.evaluate(expression, jsonNode));
    assertEquals(expected, retVal);
  }

  private static Stream<Arguments> provideExpressions() {
    return Stream.of(
        Arguments.of(FileUtil.readResourceFromClasspath("true_expression.txt"), true),
        Arguments.of(FileUtil.readResourceFromClasspath("false_expression.txt"), false));
  }
}
