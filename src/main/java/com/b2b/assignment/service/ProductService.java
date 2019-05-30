package com.b2b.assignment.service;


import com.b2b.assignment.domain.Product;
import com.b2b.assignment.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing products.
 *
 */
@Service
@Transactional
public class ProductService {

  private final Logger log = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Get all products
   * @return a list of products
   */
  @Transactional(readOnly = true)
  public List<Product> getAll() {
    return productRepository.findAll();
  }

  /**
   * Save product
   * @param product
   * @return a product
   */
  public Product save(Product product) {
    log.debug("Saved Product: {}", product);
    return productRepository.save(product);
  }

  /**
   * Find by productId
   * @param productId
   * @return a product
   */
  @Transactional(readOnly = true)
  public Product findById(Long productId) {
    return productRepository.findById(productId).orElse(null);
  }

  /**
   * Delete by productId
   * @param productId
   */
  public void delete(Long productId) {
    productRepository.deleteById(productId);
    log.debug("Deleted Product with id: {}", productId);
  }

  /**
   * Search By Either title or description
   *
   * @param query
   * @return a list of products
   */
  @Transactional(readOnly = true)
  public List<Product> search(String query) {
    return productRepository.findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(query, query);
  }

}
