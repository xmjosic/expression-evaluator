package hr.xmjosic.expressionevaluator.service;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hr.xmjosic.expressionevaluator.dao.ExpressionRepository;
import hr.xmjosic.expressionevaluator.model.Expression;
import hr.xmjosic.expressionevaluator.service.impl.ExpressionServiceImpl;
import hr.xmjosic.expressionevaluator.util.ExpressionParser;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpressionServiceTest {

  private ExpressionService service;
  @Mock private ExpressionRepository repository;
  @Mock private ExpressionParser parser;

  @BeforeEach
  void setUp() {
    service = new ExpressionServiceImpl(repository, parser);
  }

  @Test
  void expression() {
    Expression expression = new Expression();
    expression.setIdentifier(UUID.randomUUID());

    Mockito.when(repository.saveAndFlush(Mockito.any(Expression.class))).thenReturn(expression);

    String retVal = assertDoesNotThrow(() -> service.expression("test", "test"));
    Assertions.assertEquals(expression.getIdentifier().toString(), retVal);
    Mockito.verify(repository, Mockito.times(1)).saveAndFlush(Mockito.any(Expression.class));
  }

  @Test
  void evaluate() {
    String expressionName = "Complex logical expression";
    String expressionValue =
        "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")";
    Expression expression = new Expression();
    expression.setIdentifier(UUID.randomUUID());
    expression.setName(expressionName);
    expression.setValue(expressionValue);
    ObjectNode jsonInput = JsonNodeFactory.instance.objectNode();

    Mockito.when(repository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(expression));
    Mockito.when(parser.evaluate(Mockito.anyString(), Mockito.any(JsonNode.class)))
        .thenReturn(true);

    boolean retVal =
        assertDoesNotThrow(() -> service.evaluate(expression.getIdentifier(), jsonInput));
    Assertions.assertTrue(retVal);
    Mockito.verify(parser, Mockito.times(1)).evaluate(expressionValue, jsonInput);
  }

  @Test
  void evaluateWithException() {
    Mockito.when(repository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

    NoSuchElementException retVal =
        assertThrows(NoSuchElementException.class, () -> service.evaluate(UUID.randomUUID(), null));
    Assertions.assertEquals("Expression not found", retVal.getMessage());
    Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any(UUID.class));
  }
}
