package hr.xmjosic.expressionevaluator.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import hr.xmjosic.expressionevaluator.dto.ExpressionDto;
import hr.xmjosic.expressionevaluator.service.ExpressionService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpressionEvaluatorControllerTest {

  private ExpressionEvaluatorController controller;
  @Mock private ExpressionService service;

  @BeforeEach
  void setUp() {
    controller = new ExpressionEvaluatorController(service);
  }

  @Test
  void expression() {
    ExpressionDto dto = new ExpressionDto("name", "value");
    String identifier = "identifier";

    Mockito.when(service.expression(dto.name(), dto.value())).thenReturn(identifier);

    String retVal = assertDoesNotThrow(() -> controller.expression(dto));
    assertEquals(identifier, retVal);
    Mockito.verify(service, Mockito.times(1)).expression(dto.name(), dto.value());
  }

  @Test
  void evaluate() {
    UUID identifier = UUID.randomUUID();
    JsonNode json = JsonNodeFactory.instance.objectNode();

    Mockito.when(service.evaluate(identifier, json)).thenReturn(true);

    boolean retVal = assertDoesNotThrow(() -> controller.evaluate(identifier, json));
    assertTrue(retVal);
    Mockito.verify(service, Mockito.times(1)).evaluate(identifier, json);
  }
}
