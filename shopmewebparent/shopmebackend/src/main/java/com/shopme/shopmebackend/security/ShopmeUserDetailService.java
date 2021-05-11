package com.shopme.shopmebackend.security;

import com.shopme.common.entity.User;
import com.shopme.shopmebackend.user.UserRepository;
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
public class ShopmeUserDetailService  implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userByEmail = userRepository.getUserByEmail(email);
        if(userByEmail != null){
            return new ShopmeUserDetails(userByEmail);
        }
        throw  new UsernameNotFoundException("Could not fund user with email: " + email);
    }
}
