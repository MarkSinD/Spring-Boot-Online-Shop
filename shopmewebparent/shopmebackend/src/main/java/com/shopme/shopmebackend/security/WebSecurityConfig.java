package com.shopme.shopmebackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        return new ShopmeUserDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**")
                    .hasAuthority("Admin")
                .antMatchers("/categories/**", "/brands/**")
                    .hasAnyAuthority("Admin", "Editor")
                .antMatchers("/products/new", "/products/delete/**")
                    .hasAnyAuthority("Admin", "Editor") // 3 и 6
                .antMatchers("/products/edit/**", "/products/save", "/products/check_unique") // 4 5
                    .hasAnyAuthority("Admin", "Editor", "Salesperson")
                .antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
                    .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
                    .anyRequest().authenticated()
                    .and()
                        .formLogin()
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll()
                    .and()
                        .logout().permitAll()
                    .and()
                        .rememberMe();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
