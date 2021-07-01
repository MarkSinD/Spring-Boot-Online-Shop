package com.shopme.shopmebackend.customer;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.shopmebackend.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Service
@Transactional
public class CustomerService {
    public static final int CUSTOMER_PER_PAGE = 4;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer getCustomerByEmail(String email){
        return customerRepository.getCustomerByEmail(email);
    }

    public List<Customer> listAllCustomers(){
        return (List<Customer>) customerRepository.findAll(Sort.by("firstName").ascending());
    }

    public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMER_PER_PAGE, sort);

        if(keyword != null){
            return customerRepository.findAll(keyword,pageable);
        }
        return customerRepository.findAll(pageable);
    }

    public Customer save(Customer customer) {
        boolean isUpdatingCustomer = (customer.getId() != null); // true Azer
        if(isUpdatingCustomer){
            Customer existingCustomer = customerRepository.findById(customer.getId()).get();
            if(customer.getPassword().isEmpty()){
                customer.setPassword(existingCustomer.getPassword());
            }
            else{
                encodePassword(customer);
            }
        }else{
            encodePassword(customer);
        }
        return customerRepository.save(customer);
    }

    public Customer updateAccount(Customer customer){
        Customer customerInDB = customerRepository.findById(customer.getId()).get();
        if(!customer.getPassword().isEmpty()){
            customerInDB.setPassword(customer.getPassword());
            encodePassword(customerInDB);
        }

        customerInDB.setFirstName(customer.getFirstName());
        customerInDB.setLastName(customer.getLastName());

        return customerRepository.save(customerInDB);
    }

    private void encodePassword(Customer customer){
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email){
        Customer customerByEmail = customerRepository.getCustomerByEmail(email);

        System.out.println("id : " + id + ". email : " + email);
        if( id != null && customerByEmail == null) {
            return true;
        }
        else if( id != null && customerByEmail != null) {
            if(!customerByEmail.getId().equals(id))
                return false;
            else
                return true;
        }
        else if( id == null && customerByEmail != null) {
            return false;
        }
        else if(id == null && customerByEmail == null) {
            return true;
        }
        return false;
    }

    public Customer getCustomerById(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepository.findById(id).get();
        } catch (NoSuchElementException exception){
            throw new CustomerNotFoundException("Could not find any customer with ID");
        }
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long countById = customerRepository.countById(id);
        if(countById == null || countById == 0){
            throw new CustomerNotFoundException("Could not find any customer with ID" + id);
        }
        customerRepository.deleteById(id);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled){
        customerRepository.updateEnabledStatus(id, enabled);
    }
}
