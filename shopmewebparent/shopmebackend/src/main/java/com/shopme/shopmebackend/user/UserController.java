package com.shopme.shopmebackend.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.shopmebackend.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("")
    public String listAll(Model model){
        List<User> listUsers = service.listAllUsers();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/new")
    public String newUser(Model model){
        List<Role> roleList = service.listAllRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", roleList);
        model.addAttribute("pageTitle", "Create New User");

        return "user_form";
    }

    @PostMapping("/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image")MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User savedUser = service.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else{
            if(user.getPhotos().isEmpty())
                user.setPhotos(null);
            service.save(user);
        }


        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){

        try {
            User user = service.getUserById(id);
            List<Role> roleList = service.listAllRoles();
            model.addAttribute("listRoles", roleList);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User  (ID: " + id + ")");


            return "user_form";
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            List<Role> roleList = service.listAllRoles();
            redirectAttributes.addFlashAttribute("message", "The user ID "
                    + id + " has been deleted successfully");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateUserEnableStatus(@PathVariable("id") Integer id,
                                         @PathVariable("status") boolean enabled,
                                         RedirectAttributes redirectAttributes){
        service.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";

    }
}
