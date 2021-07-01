package com.shopme.shopmebackend.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
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
import java.util.Optional;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Service
@Transactional
public class UserService {
    public static final int USERS_PER_PAGE = 4;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public List<User> listAllUsers(){
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

        if(keyword != null){
            return userRepository.findAll(keyword,pageable);
        }
        return userRepository.findAll(pageable);
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null); // true Azer
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

    public User updateAccount(User userInForm){
        User userInDB = userRepository.findById(userInForm.getId()).get();
        if(!userInForm.getPassword().isEmpty()){
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }
        if(userInForm.getPhotos() != null){
            userInDB.setPhotos(userInForm.getPhotos());
        }
        userInDB.setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());

        return userRepository.save(userInDB);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email){
        User userByEmail = userRepository.getUserByEmail(email);

        if( id != null && userByEmail == null) {
            return true;
        }
        else if( id != null && userByEmail != null) {
            if(!userByEmail.getId().equals(id))
                return false;
            else
                return true;
        }
        else if( id == null && userByEmail != null) {
            return false;
        }
        else if(id == null && userByEmail == null) {
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
