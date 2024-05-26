package hr.xmjosic.expressionevaluator.dao;

import hr.xmjosic.expressionevaluator.model.Expression;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The ExpressionRepository class is a repository interface that extends JpaRepository. It is
 * responsible for performing CRUD operations on the Expression entity.
 */
@Repository
public interface ExpressionRepository extends JpaRepository<Expression, UUID> {}
