package com.b2b.assignment.domain;

import com.b2b.assignment.domain.enumeration.DietaryFlag;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Product.
 *
 */
@Entity
@Table(name="product")
public class Product implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "vendor_uid")
  Long vendorUid;

  @Column
  String title;

  @Column
  String description;

  @Column
  Long price;

  @Column
  String image;

  @Enumerated(EnumType.STRING)
  DietaryFlag dietaryFlag;

  @Column(name = "number_of_views")
  Long numberOfViews;

  @Column(name = "number_of_impressions")
  Long numberOfImpressions;

  public Product() {
  }

  public Product(String title, String description, String image, DietaryFlag dietaryFlag) {
    this.title = title;
    this.description = description;
    this.image = image;
    this.dietaryFlag = dietaryFlag;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVendorUid() {
    return vendorUid;
  }

  public void setVendorUid(Long vendorUid) {
    this.vendorUid = vendorUid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Long getNumberOfViews() {
    return numberOfViews;
  }

  public void setNumberOfViews(Long numberOfViews) {
    this.numberOfViews = numberOfViews;
  }

  public Long getNumberOfImpressions() {
    return numberOfImpressions;
  }

  public void setNumberOfImpressions(Long numberOfImpressions) {
    this.numberOfImpressions = numberOfImpressions;
  }

  public DietaryFlag getDietaryFlag() {
    return dietaryFlag;
  }

  public void setDietaryFlag(DietaryFlag dietaryFlag) {
    this.dietaryFlag = dietaryFlag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return id.equals(product.id) &&
            Objects.equals(vendorUid, product.vendorUid) &&
            Objects.equals(title, product.title) &&
            Objects.equals(price, product.price) &&
            Objects.equals(numberOfViews, product.numberOfViews) &&
            Objects.equals(numberOfImpressions, product.numberOfImpressions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, vendorUid, title, price, numberOfViews, numberOfImpressions);
  }

  @Override
  public String toString() {
    return "Transaction [id=" + id + ", vendorUid=" + vendorUid + ", title=" + title + ", price=" + price + ", image=" + image +
            ", dietaryFlag=" + dietaryFlag + ", numberOfViews=" + numberOfViews + ", numberOfImpressions=" + numberOfImpressions + "]";
  }

}
