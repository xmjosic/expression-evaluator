package hr.xmjosic.expressionevaluator.util;

import hr.xmjosic.expressionevaluator.dto.Path;
import hr.xmjosic.expressionevaluator.model.TokenType;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/** A class that tokenizes an input string into a queue of tokens. */
@Component
public class Tokenizer {

  /** The regular expression pattern used to match tokens in the input string. */
  private static final String REGEX =
      "(?=\\(|\\)|&&|<=|>=|<|>|!=|==|!)|(?<=\\(|\\)|&&|<=|>=|<|>|!=|==|!)";

  /**
   * A map that maps string representations of token types to their corresponding TokenType enum
   * values.
   */
  private static final Map<String, TokenType> tokenMap;

  static {
    tokenMap = new HashMap<>();
    tokenMap.put(TokenType.LEFT_PARENTHESIS.getCode(), TokenType.LEFT_PARENTHESIS);
    tokenMap.put(TokenType.RIGHT_PARENTHESIS.getCode(), TokenType.RIGHT_PARENTHESIS);
    tokenMap.put(TokenType.EQUAL_OPERATOR.getCode(), TokenType.EQUAL_OPERATOR);
    tokenMap.put(TokenType.NOT_EQUAL_OPERATOR.getCode(), TokenType.NOT_EQUAL_OPERATOR);
    tokenMap.put(TokenType.GREATER_OPERATOR.getCode(), TokenType.GREATER_OPERATOR);
    tokenMap.put(TokenType.GREATER_EQUAL_OPERATOR.getCode(), TokenType.GREATER_EQUAL_OPERATOR);
    tokenMap.put(TokenType.LESS_OPERATOR.getCode(), TokenType.LESS_OPERATOR);
    tokenMap.put(TokenType.LESS_EQUAL_OPERATOR.getCode(), TokenType.LESS_EQUAL_OPERATOR);
    tokenMap.put(TokenType.AND.getCode(), TokenType.AND);
    tokenMap.put(TokenType.OR.getCode(), TokenType.OR);
    tokenMap.put(TokenType.NOT.getCode(), TokenType.NOT);
  }

  /**
   * Tokenizes the input string into a queue of objects.
   *
   * @param input the input string to be tokenized
   * @return a queue of objects representing the tokens
   */
  public Queue<Object> tokenize(String input) {
    input = replaceLetterLogicalOperators(input);
    Queue<Object> retVal = new LinkedList<>();
    String[] tokens = input.trim().split(REGEX);
    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i];
      if (StringUtils.isNotBlank(token)) {
        token = token.trim();
        if (tokenMap.containsKey(token)) {
          if (tokens.length > i + 1) {
            String nextToken = tokens[i + 1].trim();
            if ("=".equals(nextToken)
                && ("<".equals(token) || ">".equals(token) || "!".equals(token))) {
              token += nextToken;
              i++;
            }
          }
          retVal.add(tokenMap.get(token));
          continue;
        }
        if (token.startsWith("\"") && token.endsWith("\"")) {
          retVal.add(token.substring(1, token.length() - 1));
          continue;
        }
        if ("true".equalsIgnoreCase(token) || "false".equalsIgnoreCase(token)) {
          retVal.add(Boolean.parseBoolean(token));
          continue;
        }
        if (StringUtils.isNumeric(token)) {
          retVal.add(Double.parseDouble(token));
          continue;
        }
        if ("null".equalsIgnoreCase(token)) {
          retVal.add(null);
          continue;
        }
        retVal.add(new Path(token));
      }
    }
    return retVal;
  }

  /**
   * Replaces letter logical operators in the input string with their corresponding code values.
   *
   * @param input the input string to be processed
   * @return the input string with letter logical operators replaced by code values
   */
  private String replaceLetterLogicalOperators(String input) {
    String retVal = input;
    if (StringUtils.startsWithIgnoreCase(retVal, "not ")
        || StringUtils.startsWithIgnoreCase(retVal, "not(")) {
      retVal = TokenType.NOT.getCode() + retVal.substring(3);
    }
    if (StringUtils.containsIgnoreCase(retVal, " and ")) {
      retVal = StringUtils.replaceIgnoreCase(retVal, " and ", " " + TokenType.AND.getCode() + " ");
    }
    if (StringUtils.containsIgnoreCase(retVal, " and(")) {
      retVal = StringUtils.replaceIgnoreCase(retVal, " and(", " " + TokenType.AND.getCode() + "(");
    }
    if (StringUtils.containsIgnoreCase(retVal, " or ")) {
      retVal = StringUtils.replaceIgnoreCase(retVal, " or ", " " + TokenType.OR.getCode() + " ");
    }
    if (StringUtils.containsIgnoreCase(retVal, " or(")) {
      retVal = StringUtils.replaceIgnoreCase(retVal, " or(", " " + TokenType.OR.getCode() + "(");
    }
    if (StringUtils.containsIgnoreCase(retVal, " not ")) {
      retVal = StringUtils.replaceIgnoreCase(retVal, " not ", " " + TokenType.NOT.getCode() + " ");
    }
    if (StringUtils.containsIgnoreCase(retVal, " not(")) {
      retVal = StringUtils.replaceIgnoreCase(retVal, " not(", " " + TokenType.NOT.getCode() + "(");
    }
    return retVal;
  }

  /**
   * Parses a string input into a list of components.
   *
   * @param input the input string to be parsed
   * @return a list of objects representing the components of the input string
   */
  public List<Object> parseString(String input) {
    List<Object> components = new ArrayList<>();
    Pattern pattern = Pattern.compile("(\\w+)|(\\[\\d+\\])");
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      String match = matcher.group();
      if (match.startsWith("[")) {
        components.add(Integer.parseInt(match.substring(1, match.length() - 1)));
      } else {
        components.add(match);
      }
    }

    return components;
  }
}
