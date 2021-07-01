package com.shopme.shopmebackend.customer;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.shopmebackend.setting.country.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("")
    public String listAll(Model model){
        return listByPage(1, model, "firstName", "asc",null);
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum")int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword ){
        Page<Customer> page = service.listByPage(pageNum, sortField, sortDir, keyword);
        List<Customer> listCustomers = page.getContent();
        long startCount = (pageNum - 1) * CustomerService.CUSTOMER_PER_PAGE + 1;
        long endCount = startCount + CustomerService.CUSTOMER_PER_PAGE - 1;
        if(endCount > page.getTotalPages()){
            endCount = page.getTotalPages();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "customer/customer";
    }

    @GetMapping("/new")
    public String newCustomer(Model model){
        Customer customer = new Customer();
        customer.setEnabled(true);
        List<Country> countryList = (List<Country>) countryRepository.findAll();
        model.addAttribute("countryList", countryList);
        model.addAttribute("customer", customer);
        model.addAttribute("pageTitle", "Create New Customer");

        return "customer/customer_form";
    }

    @PostMapping("/save")
    public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes) throws IOException {
        service.save(customer);
        redirectAttributes.addFlashAttribute("message", "The customer has been saved successfully");
        return getRedirectURLtoAffectedUser(customer);
    }

    private String getRedirectURLtoAffectedUser(Customer customer) {
        String firstPartOfEmail = customer.getEmail().split("@")[0];
        return "redirect:/customers/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try {
            Customer customer = service.getCustomerById(id);
            List<Country> countryList = (List<Country>) countryRepository.findAll();
            model.addAttribute("countryList", countryList);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
            return "customer/customer_form";

        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The customer ID "
                    + id + " has been deleted successfully");
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/customers";
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateCustomerEnableStatus(@PathVariable("id") Integer id,
                                         @PathVariable("status") boolean enabled,
                                         RedirectAttributes redirectAttributes){
        service.updateCustomerEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The customer ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/customers";
    }
}
