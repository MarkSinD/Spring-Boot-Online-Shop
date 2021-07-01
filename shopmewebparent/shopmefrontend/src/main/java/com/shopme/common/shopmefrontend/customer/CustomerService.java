package com.shopme.common.shopmefrontend.customer;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.shopmefrontend.settings.CountryRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Service
@Transactional
public class CustomerService {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Country> listAllCuntries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email){
        Customer customer = customerRepository.findByEmail(email);
        return customer == null;
    }

    public void registerCustomer(Customer customer){
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);
        customerRepository.save(customer);
        System.out.println("Verification code :" + randomCode);
    }

    public Customer getCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    private void encodePassword(Customer customer){
        String encodePassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodePassword);
    }

    public boolean verify(String verificationCode){
        Customer customer = customerRepository.findByVerificationCode(verificationCode);
        if (customer == null || customer.getEnabled()) {
            return false;
        }
        else{
            customerRepository.enable(customer.getId());
            return true;
        }
    }

    public void updateAuthenticationType(Customer customer, AuthenticationType type){
        if(!customer.getAuthenticationType().equals(type)){
            customerRepository.updateAuthenticationType(customer.getId(), type);
        }
    }

    public void addNewCustomerUponOAuthLohin(String name, String email, String countryCode) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setFirstName(name);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setState("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        customer.setCountry(countryRepository.findByCode(countryCode));

        customerRepository.save(customer);
    }

    private void setName(String name, Customer customer){
        String[] nameArray = name.split(" ");
        if(nameArray.length < 2){
            customer.setFirstName(name);
            customer.setLastName("");
        }
        else{
            String firstName = nameArray[0];
            customer.setFirstName(firstName);

            String lastName = name.replaceFirst(firstName, "");
            customer.setLastName(lastName);
        }
    }
}
