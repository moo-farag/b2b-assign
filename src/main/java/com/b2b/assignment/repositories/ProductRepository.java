package com.b2b.assignment.repositories;

import com.b2b.assignment.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Search By Either title or description
     *
     * The famous spring data jpa issue for using OR (expects 2 queries passed)
     *
     * @param titleQuery
     * @param descriptionQuery
     * @return a list of products
     */
    List<Product> findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String titleQuery, String descriptionQuery);
}
