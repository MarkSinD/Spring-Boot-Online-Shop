package com.shopme.shopmebackend.product;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class ProductNotFoundException extends Throwable {
    public ProductNotFoundException(String s) {
        super(s);
    }
}