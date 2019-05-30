package com.b2b.assignment.web.rest;

import com.b2b.assignment.B2bFoodGroupApplication;
import com.b2b.assignment.domain.Product;
import com.b2b.assignment.domain.enumeration.DietaryFlag;
import com.b2b.assignment.repositories.ProductRepository;
import com.b2b.assignment.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = B2bFoodGroupApplication.class)
public class ProductResourceIntTest {

  private static final String DEFAULT_TITLE = "1st Title";
  private static final String UPDATED_TITLE = "2nd Title";

  private static final String DEFAULT_DESCRIPTION = "1st Description";
  private static final String UPDATED_DESCRIPTION = "2nd Description";

  private static final String DEFAULT_IMAGE_URL = "1st Image";
  private static final String UPDATED_IMAGE_URL = "2nd Image";

  private static final DietaryFlag DEFAULT_DIETARY_FLAG = DietaryFlag.VEGAN;
  private static final DietaryFlag UPDATED_DIETARY_FLAG = DietaryFlag.LACTOSE_FREE;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductService productService;

  private MockMvc restProductMockMvc;

  @Autowired
  private EntityManager em;

  private Product product;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ProductResource productResource = new ProductResource(productService);
    this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource).build();
  }

  /**
   * Create an entity for this test.
   *
   */
  public static Product createEntity() {
    return new Product(DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_IMAGE_URL, DEFAULT_DIETARY_FLAG);
  }

  @Before
  public void initTest() {
    product = createEntity();
  }

  @Test
  @Transactional
  public void createProduct() throws Exception {
    int databaseSizeBeforeCreate = productRepository.findAll().size();

    // Create the Product
    restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

    // Validate the Product in the database
    List<Product> productList = productRepository.findAll();
    assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
    Product testProduct = productList.get(productList.size() - 1);
    assertThat(testProduct.getTitle()).isEqualTo(DEFAULT_TITLE);
    assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE_URL);
    assertThat(testProduct.getDietaryFlag()).isEqualTo(DEFAULT_DIETARY_FLAG);

  }

  @Test
  @Transactional
  public void getAllProducts() throws Exception {
    // Initialize the database
    productRepository.saveAndFlush(product);

    // Get all the productList
    restProductMockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].dietaryFlag").value(hasItem(DEFAULT_DIETARY_FLAG.toString())));
  }

  @Test
  @Transactional
  public void getNonExistingProduct() throws Exception {
    // Get the product
    restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateProduct() throws Exception {

    productService.save(product);

    int databaseSizeBeforeUpdate = productRepository.findAll().size();

    // Update the product
    Product updatedProduct = productRepository.findById(product.getId()).get();

    // Disconnect from session so that the updates on updatedProduct are not directly saved in db
    em.detach(updatedProduct);

    updatedProduct.setTitle(UPDATED_TITLE);
    updatedProduct.setDescription(UPDATED_DESCRIPTION);
    updatedProduct.setImage(UPDATED_IMAGE_URL);
    updatedProduct.setDietaryFlag(UPDATED_DIETARY_FLAG);

    restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduct)))
            .andExpect(status().isOk());

    // Validate the Product in the database
    List<Product> productList = productRepository.findAll();
    assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    Product testProduct = productList.get(productList.size() - 1);
    assertThat(testProduct.getTitle()).isEqualTo(UPDATED_TITLE);
    assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testProduct.getImage()).isEqualTo(UPDATED_IMAGE_URL);
    assertThat(testProduct.getDietaryFlag()).isEqualTo(UPDATED_DIETARY_FLAG);

  }

  @Test
  @Transactional
  public void updateNonExistingProduct() throws Exception {
    int databaseSizeBeforeUpdate = productRepository.findAll().size();

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

    // Validate the Product in the database
    List<Product> productList = productRepository.findAll();
    assertThat(productList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteProduct() throws Exception {
    // Initialize the database
    productService.save(product);

    int databaseSizeBeforeDelete = productRepository.findAll().size();

    // Get the product
    restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

    // Validate the database is empty
    List<Product> productList = productRepository.findAll();
    assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void searchProductByTitle() throws Exception {
    // Initialize the database
    productService.save(product);

    // Search the product
    restProductMockMvc.perform(get("/api/products/search/" + product.getTitle()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(product.getTitle())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(product.getDescription())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(product.getImage())))
            .andExpect(jsonPath("$.[*].dietaryFlag").value(hasItem(product.getDietaryFlag().toString())));

  }

  @Test
  @Transactional
  public void searchProductByDescription() throws Exception {
    // Initialize the database
    productService.save(product);

    // Search the product
    restProductMockMvc.perform(get("/api/products/search/" + product.getDescription()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(product.getTitle())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(product.getDescription())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(product.getImage())))
            .andExpect(jsonPath("$.[*].dietaryFlag").value(hasItem(product.getDietaryFlag().toString())));

  }

}
