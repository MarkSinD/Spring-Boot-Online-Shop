package com.shopme.shopmebackend.setting.country;

import com.shopme.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@RestController
@RequestMapping("/countries")
public class CountryRestController {

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/list")
    public List<Country> listAll(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    @PostMapping("/save")
    public String save(@RequestBody Country country) {
        Country savedCountry = countryRepository.save(country);
        return String.valueOf(savedCountry.getId());
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        countryRepository.deleteById(id);
    }
}
