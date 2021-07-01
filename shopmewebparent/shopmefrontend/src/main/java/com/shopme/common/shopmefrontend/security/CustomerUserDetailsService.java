package com.shopme.common.shopmefrontend.security;

import com.shopme.common.entity.Customer;
import com.shopme.common.shopmefrontend.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = repository.findByEmail(email);
        if (customer==null){
            throw new UsernameNotFoundException("No customer found with the email " + email);
        }
        return new CustomerUserDetails(customer);
    }
}
