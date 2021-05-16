package com.shopme.common.entity;

import javax.persistence.*;
import java.util.*;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 256, nullable = false)
    private String name;

    @Column(unique = true, length = 256, nullable = false)
    private String alias;

    @Column(length = 512, nullable = false, name = "short_description")
    private String shortDescription;

    @Column(length = 4096, nullable = false, name = "full_description")
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean enabled;

    @Column(name = "in_stock")
    private boolean inStock;

    private float cost;

    private float prise;

    @Column(name = "discount_percent")
    private float discountPercent;

    private float lenght;
    private float width;
    private float height;
    private float weight;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getPrise() {
        return prise;
    }

    public void setPrise(float prise) {
        this.prise = prise;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float descountPercent) {
        this.discountPercent = descountPercent;
    }

    public float getLenght() {
        return lenght;
    }

    public void setLenght(float lenght) {
        this.lenght = lenght;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }

    public void addExtraImage(String imageName){
        this.images.add(new ProductImage(imageName, this));
    }

    public void addProductDetail(String name, String value){
        ProductDetail productDetail = new ProductDetail(name, value, this);
        this.details.add(productDetail);
    }

    public void addProductDetail(Integer id, String name, String value){
        ProductDetail productDetail = new ProductDetail(id, name, value, this);
        this.details.add(productDetail);
    }

    public List<ProductDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }

    @Transient
    public String getMainImagePath(){
        if(id == null || mainImage == null)
            return "/images/image-thumbnail.png";

        System.out.println("/product-images/" + this.id + "/" + this.mainImage);
        return "/product-images/" + this.id + "/" + this.mainImage;
    }

    public boolean contrainsImageName(String fileName) {
        Iterator<ProductImage> iterator = images.iterator();

        while(iterator.hasNext()){
            ProductImage image = iterator.next();
            if(image.getName().equals(fileName)){
                return true;
            }
        }
        return false;
    }

    @Transient
    public String getShortName(){
        if(name.length() > 70)
            return name.substring(0,70).concat("..");
        return name;
    }

    @Transient
    public float getDiscountPrise(){
        if(discountPercent > 0){
            return prise * ((100 - discountPercent)/100);
        }
        return this.prise;
    }
}
