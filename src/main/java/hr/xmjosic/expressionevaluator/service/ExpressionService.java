package hr.xmjosic.expressionevaluator.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;

/** A service interface for managing expressions. */
public interface ExpressionService {

  /**
   * Creates a new expression with the given name and value.
   *
   * @param expressionName the name of the expression
   * @param expressionValue the value of the expression
   * @return the identifier of the created expression
   */
  String expression(String expressionName, String expressionValue);

  /**
   * Evaluates the given expression using the provided JSON input.
   *
   * @param expressionIdentifier the identifier of the expression to evaluate
   * @param jsonInput the JSON input to evaluate against
   * @return true if the expression evaluates to true, false otherwise
   */
  boolean evaluate(UUID expressionIdentifier, JsonNode jsonInput);
}
