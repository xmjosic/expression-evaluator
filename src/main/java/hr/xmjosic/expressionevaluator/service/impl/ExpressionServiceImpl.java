package hr.xmjosic.expressionevaluator.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import hr.xmjosic.expressionevaluator.dao.ExpressionRepository;
import hr.xmjosic.expressionevaluator.model.Expression;
import hr.xmjosic.expressionevaluator.service.ExpressionService;
import hr.xmjosic.expressionevaluator.util.ExpressionParser;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The ExpressionServiceImpl class is an implementation of the ExpressionService interface. It
 * provides methods for evaluating and parsing expressions.
 */
@Service
@RequiredArgsConstructor
public class ExpressionServiceImpl implements ExpressionService {

  /** The repository for managing {@link Expression} objects. */
  private final ExpressionRepository repository;

  /** The parser used to evaluate expressions. */
  private final ExpressionParser parser;

  @Override
  public String expression(String expressionName, String expressionValue) {
    Expression expression = new Expression();
    expression.setName(expressionName);
    expression.setValue(expressionValue);
    Expression save = repository.saveAndFlush(expression);
    return save.getIdentifier().toString();
  }

  @Override
  public boolean evaluate(UUID expressionIdentifier, JsonNode jsonInput) {
    Expression byId =
        repository
            .findById(expressionIdentifier)
            .orElseThrow(() -> new NoSuchElementException("Expression not found"));
    return parser.evaluate(byId.getValue(), jsonInput);
  }
}
