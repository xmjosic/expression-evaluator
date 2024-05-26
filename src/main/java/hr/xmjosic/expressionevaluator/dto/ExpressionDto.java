package hr.xmjosic.expressionevaluator.dto;

/**
 * used to transfer data about an expression
 *
 * @param name expression name
 * @param value expression value
 */
public record ExpressionDto(String name, String value) {}
