package hr.xmjosic.expressionevaluator;

import hr.xmjosic.expressionevaluator.dao.ExpressionRepository;
import hr.xmjosic.expressionevaluator.dto.ErrorDto;
import hr.xmjosic.expressionevaluator.dto.ExpressionDto;
import hr.xmjosic.expressionevaluator.model.Expression;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import static java.util.Objects.nonNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
class ExpressionEvaluatorApplicationIntegrationTests {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate client;

  @Autowired private ExpressionRepository repository;

  @SneakyThrows
  @Test
  void addExpression() {
    String name = "Complex logical expression";
    String value =
        "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")";

    ResponseEntity<String> exchange =
        client.exchange(
            "/api/v1/expression",
            HttpMethod.POST,
            new HttpEntity<>(new ExpressionDto(name, value)),
            String.class);

    Assertions.assertNotNull(exchange);
    Assertions.assertNotNull(exchange.getStatusCode());
    Assertions.assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
    Assertions.assertNotNull(exchange.getBody());
    UUID id = Assertions.assertDoesNotThrow(() -> UUID.fromString(exchange.getBody()));

    Optional<Expression> byId = repository.findById(id);

    Assertions.assertTrue(byId.isPresent());
    Assertions.assertEquals(name, byId.get().getName());
    Assertions.assertEquals(value, byId.get().getValue());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidArgumentsForAddExpression")
  void addExpressionWithException(String path, HttpEntity<?> request, HttpStatus status) {
    ResponseEntity<ErrorDto> exchange =
        client.exchange(path, HttpMethod.POST, request, ErrorDto.class);

    Assertions.assertNotNull(exchange);
    Assertions.assertEquals(status, exchange.getStatusCode());
  }

  private static Stream<Arguments> provideInvalidArgumentsForAddExpression() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return Stream.of(
        Arguments.of("/api/v1/expression", null, HttpStatus.UNSUPPORTED_MEDIA_TYPE),
        Arguments.of("/api/v1/expression", new HttpEntity<>(httpHeaders), HttpStatus.BAD_REQUEST));
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void evaluateExpression(
      String json, String expressionName, String expressionValue, boolean expected) {
    Expression expression = new Expression();
    expression.setName(expressionName);
    expression.setValue(expressionValue);
    expression = repository.saveAndFlush(expression);

    Assertions.assertNotNull(expression);
    Assertions.assertNotNull(expression.getIdentifier());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(json, httpHeaders);
    ResponseEntity<Boolean> retVal =
        client.exchange(
            UriComponentsBuilder.fromPath("/api/v1/evaluate")
                .pathSegment(expression.getIdentifier().toString())
                .build()
                .toString(),
            HttpMethod.POST,
            request,
            Boolean.class);

    Assertions.assertNotNull(retVal);
    Assertions.assertNotNull(retVal.getStatusCode());
    Assertions.assertEquals(HttpStatus.OK, retVal.getStatusCode());
    Assertions.assertNotNull(retVal.getBody());
    Assertions.assertEquals(expected, retVal.getBody());
  }

  private static Stream<Arguments> provideArguments() {
    String jsonInput = FileUtil.readResourceFromClasspath("jsonInput.json");
    return Stream.of(
        Arguments.of(
            jsonInput,
            "Complex logical expression",
            "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")",
            true),
        Arguments.of(
            jsonInput,
            "Complex logical expression with negation",
            "(customer.firstName == \"GEORGE\" && customer.salary < 100) OR NOT(customer.address != null && customer.address.city == \"Washington\")",
            true),
        Arguments.of(
            jsonInput,
            "Complex logical expression with negation and array",
            "NOT(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.contact[0].email == \"email1@example.com\" && customer.contact[1].email != null)",
            true),
        Arguments.of(
            jsonInput,
            "Complex logical expression with negation and array with result false",
            "NOT(customer.firstName == \"JOHN\" && customer.salary < 100) OR !(customer.contact[0].email == \"email1@example.com\" && customer.contact[1].email != null)",
            false));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidArgumentsForEvaluateExpression")
  void evaluateExpressionWithException(String path, HttpEntity<?> request, HttpStatus status, String expressionValue) {
    if (nonNull(expressionValue)) {
      Expression expression = new Expression();
      expression.setName("Complex logical expression");
      expression.setValue(expressionValue);
      expression = repository.saveAndFlush(expression);

      path += expression.getIdentifier().toString();
    }

    ResponseEntity<ErrorDto> exchange =
        client.exchange(path, HttpMethod.POST, request, ErrorDto.class);

    Assertions.assertNotNull(exchange);
    Assertions.assertEquals(status, exchange.getStatusCode());
  }

  private static Stream<Arguments> provideInvalidArgumentsForEvaluateExpression() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    String jsonInput = FileUtil.readResourceFromClasspath("jsonInput.json");
    return Stream.of(
        Arguments.of("/api/v1/evaluate", null, HttpStatus.BAD_REQUEST, null),
        Arguments.of("/api/v1/evaluate", new HttpEntity<>(httpHeaders), HttpStatus.BAD_REQUEST, null),
        Arguments.of(
            "/api/v1/evaluate/5650279c-144b-4879-8571-c9fb61275fa2",
            new HttpEntity<>(httpHeaders),
            HttpStatus.BAD_REQUEST, null),
            Arguments.of(
                    "/api/v1/evaluate/",
                    new HttpEntity<>(jsonInput, httpHeaders),
                    HttpStatus.BAD_REQUEST, "OR"));
  }
}
