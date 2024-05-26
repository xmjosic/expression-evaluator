package hr.xmjosic.expressionevaluator.model;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Represents an expression. */
@Entity
@Getter
@Setter
@ToString
public class Expression {

  /** A unique identifier for the expression, generated using UUID. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID identifier;

  /** The name of the expression. */
  private String name;

  /** The value of the expression. */
  @Column(name = "expression_value")
  private String value;
}
