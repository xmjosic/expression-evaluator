package hr.xmjosic.expressionevaluator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import hr.xmjosic.expressionevaluator.dto.ExpressionDto;
import hr.xmjosic.expressionevaluator.service.ExpressionService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for evaluating expressions.
 *
 * <p>This class handles HTTP POST requests to evaluate expressions identified by a UUID identifier.
 * The expressions are evaluated using the provided JSON input.
 *
 * @see ExpressionService
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExpressionEvaluatorController {

  private final ExpressionService service;

  /**
   * Creates a new expression using the provided data.
   *
   * @param dto the DTO containing the name and value of the expression
   * @return the created expression identifier as a string
   */
  @PostMapping("/expression")
  @ResponseStatus(HttpStatus.CREATED)
  public String expression(@RequestBody ExpressionDto dto) {
    return this.service.expression(dto.name(), dto.value());
  }

  /**
   * Evaluates the given expression using the provided JSON input.
   *
   * @param identifier the identifier of the expression to evaluate
   * @param json the JSON input to evaluate against
   * @return true if the expression evaluates to true, false otherwise
   */
  @PostMapping("/evaluate/{identifier}")
  @ResponseStatus(HttpStatus.OK)
  public Boolean evaluate(@PathVariable UUID identifier, @RequestBody JsonNode json) {
    return this.service.evaluate(identifier, json);
  }
}
