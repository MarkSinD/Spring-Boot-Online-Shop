package com.shopme.shopmebackend.category;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */

public class CategoryNotFoundException extends Throwable {
    public CategoryNotFoundException(String s) {
        super(s);
    }
}
