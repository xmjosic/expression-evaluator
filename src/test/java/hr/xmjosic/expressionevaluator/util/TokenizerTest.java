package hr.xmjosic.expressionevaluator.util;

import static org.junit.jupiter.api.Assertions.*;

import hr.xmjosic.expressionevaluator.dto.Path;
import hr.xmjosic.expressionevaluator.model.TokenType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.Test;

class TokenizerTest {

  @Test
  void tokenize() {
    Tokenizer tokenizer = new Tokenizer();
    Queue<Object> result =
        tokenizer.tokenize(
            "not(a and b) or !(c > 5) or not(d >= 5) and (e < 5) or (f <= 5) && (g != 5) || (h == 5)");
    assertEquals(
        Arrays.asList(
            TokenType.NOT,
            TokenType.LEFT_PARENTHESIS,
            new Path("a"),
            TokenType.AND,
            new Path("b"),
            TokenType.RIGHT_PARENTHESIS,
            TokenType.OR,
            TokenType.NOT,
            TokenType.LEFT_PARENTHESIS,
            new Path("c"),
            TokenType.GREATER_OPERATOR,
            5.0,
            TokenType.RIGHT_PARENTHESIS,
            TokenType.OR,
            TokenType.NOT,
            TokenType.LEFT_PARENTHESIS,
            new Path("d"),
            TokenType.GREATER_EQUAL_OPERATOR,
            5.0,
            TokenType.RIGHT_PARENTHESIS,
            TokenType.AND,
            TokenType.LEFT_PARENTHESIS,
            new Path("e"),
            TokenType.LESS_OPERATOR,
            5.0,
            TokenType.RIGHT_PARENTHESIS,
            TokenType.OR,
            TokenType.LEFT_PARENTHESIS,
            new Path("f"),
            TokenType.LESS_EQUAL_OPERATOR,
            5.0,
            TokenType.RIGHT_PARENTHESIS,
            TokenType.AND,
            TokenType.LEFT_PARENTHESIS,
            new Path("g"),
            TokenType.NOT_EQUAL_OPERATOR,
            5.0,
            TokenType.RIGHT_PARENTHESIS,
            TokenType.OR,
            TokenType.LEFT_PARENTHESIS,
            new Path("h"),
            TokenType.EQUAL_OPERATOR,
            5.0,
            TokenType.RIGHT_PARENTHESIS),
        new ArrayList<>(result));
  }

  @Test
  void parseString() {
    Tokenizer tokenizer = new Tokenizer();
    List<Object> result = tokenizer.parseString("result[1].address[0].street");
    assertEquals(Arrays.asList("result", 1, "address", 0, "street"), result);
  }
}
