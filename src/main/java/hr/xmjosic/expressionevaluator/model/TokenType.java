package hr.xmjosic.expressionevaluator.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Represents the type of token in an expression. */
@RequiredArgsConstructor
@Getter
public enum TokenType {
  // parenthesis
  /** Represents an opening parenthesis token. */
  LEFT_PARENTHESIS("("),
  /** Represents a closing parenthesis token. */
  RIGHT_PARENTHESIS(")"),
  // operators
  /** Represents an equals token. */
  EQUAL_OPERATOR("=="),
  /** Represents an not equals token. */
  NOT_EQUAL_OPERATOR("!="),
  /** Represents a greater than token. */
  GREATER_OPERATOR(">"),
  /** Represents a greater than or equal token. */
  GREATER_EQUAL_OPERATOR(">="),
  /** Represents a less than token. */
  LESS_OPERATOR("<"),
  /** Represents a less than or equal token. */
  LESS_EQUAL_OPERATOR("<="),
  // logical operators
  /** Code representation of the AND operator. */
  AND("&&"),
  /** Code representation of the OR operator. */
  OR("||"),
  /** Code representation of the NOT operator. */
  NOT("!");

  /** Code representation of the token type. */
  private final String code;
}
