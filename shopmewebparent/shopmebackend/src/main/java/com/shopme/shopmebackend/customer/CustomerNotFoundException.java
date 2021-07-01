package com.shopme.shopmebackend.customer;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class CustomerNotFoundException extends Throwable {
    public CustomerNotFoundException(String s) {
        super(s);
    }
}
