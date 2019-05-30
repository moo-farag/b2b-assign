package com.b2b.assignment.web.rest;

import com.b2b.assignment.domain.Product;
import com.b2b.assignment.service.ProductService;
import com.b2b.assignment.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * REST controller for managing products.
 *
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

  private final Logger log = LoggerFactory.getLogger(ProductResource.class);

  private final ProductService productService;

  public ProductResource(ProductService productService) {
    this.productService = productService;
  }

  /**
   * GET /products : Gets all Products.
   *
   * @return the ResponseEntity with status 200 (OK) and with body list of all products
   */
  @GetMapping(path ="/products")
  public ResponseEntity<List<Product>> getProducts() {
    return ResponseEntity.ok(productService.getAll());
  }

  /**
   * POST /products : Creates a new Product.
   *
   * @param product the product to create
   * @return the ResponseEntity with status 200 (OK) and with body the created product
   */
  @PostMapping(path ="/products")
  public ResponseEntity<Product> createProduct(@RequestBody Product product) throws URISyntaxException {
    log.debug("REST request to create Product : {}", product);
    Product result = productService.save(product);
    return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
  }

  /**
   * PUT /products : Updates an existing Product.
   *
   * @param product the product to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated product
   */
  @PutMapping(path ="/products")
  public ResponseEntity<Product> updateProduct(@RequestBody Product product) throws URISyntaxException {
    if (product.getId() == null) {
      return createProduct(product);
    }
    log.debug("REST request to update Product : {}", product);
    Product result = productService.save(product);
    return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
  }

  /**
   * DELETE /products/:id : Deletes an existing Product.
   *
   * @param id the id of the product to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping(path ="/products/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable(name="id") Long id) {
    log.debug("REST request to delete Product with id : {}", id);
    productService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * GET /products/:id : Gets a Product by id.
   *
   * @param id the id of the product to get
   * @return the ResponseEntity with status 200 (OK) and with body the product
   */
  @GetMapping(path = "/products/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable(name="id") Long id){
    Product product = productService.findById(id);
    if (product!=null)
      return ResponseEntity.ok(product);
    return ResponseEntity.notFound().build();
  }

  /**
   * SEARCH /products/search/:query : search for Products by title or description corresponding
   * to the query.
   *
   * @param query the query to search in products title or description
   * @return the ResponseEntity with status 200 (OK) and with body result of the search products
   */
  @GetMapping(path = "/products/search/{query}")
  public ResponseEntity<List<Product>> searchProducts(@PathVariable(name="query") String query){
    return ResponseEntity.ok(productService.search(query));
  }

}
