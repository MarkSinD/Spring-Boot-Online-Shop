package com.shopme.shopmebackend.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if(isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }
            else{
                encodePassword(user);
            }
        }else{
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email){
        User userByEmail = userRepository.getUserByEmail(email);

        if( id != null && userByEmail == null) {
            System.out.println("EmailUnique : " +  1);
            return true;
        }
        else if( id != null && userByEmail != null) {
            System.out.println("EmailUnique : " +  2);
            return true;
        }
        else if( id == null && userByEmail != null) {
            System.out.println("EmailUnique : " +  3);
            return false;
        }
        else if(id == null && userByEmail == null) {
            System.out.println("EmailUnique : " +  4);
            return true;
        }
        return false;
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException exception){
            throw new UserNotFoundException("Could not find any user with ID");
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if(countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with ID" + id);
        }

        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepository.updateEnabledStatus(id, enabled);
    }

}
