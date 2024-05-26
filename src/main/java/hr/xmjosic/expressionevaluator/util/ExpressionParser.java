package hr.xmjosic.expressionevaluator.util;

import static hr.xmjosic.expressionevaluator.util.CastUtil.*;
import static java.util.Objects.isNull;

import com.fasterxml.jackson.databind.JsonNode;
import hr.xmjosic.expressionevaluator.dto.Path;
import hr.xmjosic.expressionevaluator.model.TokenType;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** A utility class for parsing and evaluating expressions. */
@Component
@RequiredArgsConstructor
public class ExpressionParser {

  /** The tokenizer used for parsing and tokenizing expressions. */
  private final Tokenizer tokenizer;

  /**
   * Evaluates the given expression using the provided JSON input.
   *
   * @param expression the expression to evaluate
   * @param jsonInput the JSON input to evaluate against
   * @return true if the expression evaluates to true, false otherwise
   */
  public boolean evaluate(String expression, JsonNode jsonInput) {
    Queue<Object> tokens = tokenizer.tokenize(expression);
    Queue<Object> evaluatedTokens = new LinkedList<>();
    while (!tokens.isEmpty()) {
      Object token = tokens.poll();
      if (token instanceof Path p) evaluatedTokens.add(getValue(jsonInput, p.value()));
      else evaluatedTokens.add(token);
    }
    Queue<Object> evaluatedExpressions = new LinkedList<>();
    Queue<Object> evals = new LinkedList<>();
    boolean isInsideParenthesis = false;
    boolean negate = false;
    while (!evaluatedTokens.isEmpty()) {
      Object token = evaluatedTokens.poll();

      if (token instanceof TokenType t && t == TokenType.NOT) {
        negate = true;
        continue;
      }

      if (token instanceof TokenType t && t == TokenType.LEFT_PARENTHESIS) {
        isInsideParenthesis = true;
        while (!evaluatedExpressions.isEmpty()) {
          evals.add(evaluatedExpressions.poll());
        }
        if (negate) {
          evals.add(TokenType.NOT);
          negate = false;
        }
        continue;
      }
      if (token instanceof TokenType t && t == TokenType.RIGHT_PARENTHESIS) {
        while (!evaluatedExpressions.isEmpty()) {
          Object operand1 = evaluatedExpressions.poll();
          Object operator = evaluatedExpressions.poll();
          Object operand2 = evaluatedExpressions.poll();

          if (operator instanceof TokenType tt && tt == TokenType.AND) {
            evals.add(castToPrimitiveBoolean(operand1) && castToPrimitiveBoolean(operand2));
          }
          if (operator instanceof TokenType tt && tt == TokenType.OR) {
            evals.add(castToPrimitiveBoolean(operand1) || castToPrimitiveBoolean(operand2));
          }
        }
        if (evaluatedTokens.peek() instanceof TokenType tt
            && (tt == TokenType.AND || tt == TokenType.OR)) {
          evals.add(evaluatedTokens.poll());
        }
        isInsideParenthesis = false;
        continue;
      }
      Object operator = evaluatedTokens.poll();
      Object operand2 = evaluatedTokens.poll();

      boolean eval = eval(token, operator, operand2);

      if (isInsideParenthesis) {
        evaluatedExpressions.add(eval);

        if (evaluatedTokens.peek() instanceof TokenType tt
            && (tt == TokenType.AND || tt == TokenType.OR)) {
          evaluatedExpressions.add(evaluatedTokens.poll());
        }
      } else {
        evals.add(eval);

        if (evaluatedTokens.peek() instanceof TokenType tt
            && (tt == TokenType.AND || tt == TokenType.OR)) {
          evals.add(evaluatedTokens.poll());
        }
      }
    }
    boolean retVal = false;
    if (!evals.isEmpty()) {
      boolean isNot = evals.peek() instanceof TokenType tt && tt == TokenType.NOT;
      if (isNot) evals.poll();
      retVal = isNot != castToPrimitiveBoolean(evals.poll());
      while (!evals.isEmpty()) {
        TokenType oper = castToTokenType(evals.poll());
        boolean isNot2 = evals.peek() instanceof TokenType tt && tt == TokenType.NOT;
        if (isNot2) evals.poll();
        boolean operand2 = isNot2 != castToPrimitiveBoolean(evals.poll());
        if (oper == TokenType.AND) {
          retVal = retVal && operand2;
          if (!retVal) return false;
        }
        if (oper == TokenType.OR) {
          retVal = retVal || operand2;
        }
      }
    }
    return retVal;
  }

  /**
   * Evaluates the given operands using the provided operator.
   *
   * @param operand1 the first operand
   * @param operator the operator to use for evaluation
   * @param operand2 the second operand
   * @return the result of the evaluation
   * @throws IllegalArgumentException if the operator is invalid
   */
  private boolean eval(Object operand1, Object operator, Object operand2) {
    if (!(operator instanceof TokenType op))
      throw new IllegalArgumentException("Operator invalid.");
    return switch (op) {
      case EQUAL_OPERATOR -> Objects.equals(operand1, operand2);
      case NOT_EQUAL_OPERATOR -> !Objects.equals(operand1, operand2);
      case GREATER_OPERATOR -> castToPrimitiveDouble(operand1) > castToPrimitiveDouble(operand2);
      case GREATER_EQUAL_OPERATOR ->
          castToPrimitiveDouble(operand1) >= castToPrimitiveDouble(operand2);
      case LESS_OPERATOR -> castToPrimitiveDouble(operand1) < castToPrimitiveDouble(operand2);
      case LESS_EQUAL_OPERATOR ->
          castToPrimitiveDouble(operand1) <= castToPrimitiveDouble(operand2);
      default -> false;
    };
  }

  /**
   * Retrieves the value at the specified path in the given JSON input.
   *
   * @param jsonInput the JSON input to search in
   * @param path the path to the desired value
   * @return the value at the specified path, or null if not found
   * @throws IllegalArgumentException if the path is invalid
   */
  private Object getValue(JsonNode jsonInput, String path) {
    try {
      JsonNode byPath = getByPath(jsonInput, path);
      if (isNull(byPath) || byPath.isNull() || byPath.isMissingNode()) return null;
      if (byPath.isTextual()) return byPath.asText();
      if (byPath.isNumber()) return byPath.asDouble();
      if (byPath.isBoolean()) return byPath.asBoolean();
      return byPath.toString();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid path: " + path);
    }
  }

  /**
   * Retrieves the JsonNode at the specified path in the given JsonNode input.
   *
   * @param jsonInput the JsonNode input to search in
   * @param token the path to the desired JsonNode
   * @return the JsonNode at the specified path, or null if not found
   */
  private JsonNode getByPath(JsonNode jsonInput, String token) {
    List<Object> strings = tokenizer.parseString(token);
    JsonNode node = jsonInput;
    for (Object s : strings) {
      if (!isNull(node)) {
        if (s instanceof String str) node = node.get(str);
        if (s instanceof Integer i) node = node.get(i);
      } else return null;
    }
    return node;
  }
}
