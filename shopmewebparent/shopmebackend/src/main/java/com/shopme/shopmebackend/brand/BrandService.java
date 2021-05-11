package com.shopme.shopmebackend.brand;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll() {
        List<Brand> brandList = brandRepository.findAll();
        return brandList;
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public String checkUnique(Integer id, String name){
        boolean isCreatingNow = (id == null || id == 0);

        Brand brandByName = brandRepository.findByName(name);
        // Если обьект новый, но занято
        if(isCreatingNow && brandByName != null)
            return "DuplicateName";

        // Если имя не нулевое и id не совпадают - обновляемого
        // обьекта и обекта, к которому прикреплено имя
        else if(brandByName != null && !brandByName.getId().equals(id))
            return "DuplicateName";

        return "OK";
    }

    public Brand getBrandById(Integer id) throws BrandNotFoundException {
        try{
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException exception){
            throw new BrandNotFoundException("Could not find any brand with ID");
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = brandRepository.countById(id);
        if (countById == null || countById == 0){
            throw new BrandNotFoundException("Could not find any user with ID" + id);
        }
        brandRepository.deleteById(id);
    }
}
