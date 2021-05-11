package com.shopme.shopmebackend.brand;

import com.shopme.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTest {

    @MockBean private BrandRepository repo;
    @InjectMocks private  BrandService service;

    @Test
    public void testCheckUniqueInNewModelReturnDuplicate(){
        Integer id = null;
        String name = "Acer";
        String logo = "default.png";
        Brand brand = new Brand(name, logo);

        Mockito.when(repo.findByName(name)).thenReturn(brand);
        String result = service.checkUnique(id, name);
        assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK(){
        Integer id = null;
        String name = "AMD";

        Mockito.when(repo.findByName(name)).thenReturn(null);

        String result = service.checkUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueEditReturnDuplucate(){
        Integer id = 1;
        String name = "Canon";
        String logo = "default.png";
        Brand brand = new Brand(id, name, logo);

        Mockito.when(repo.findByName(name)).thenReturn(brand);

        String result = service.checkUnique(2, "Canon");
        assertThat(result).isEqualTo("Duplicate");
    }

    @Test
    public void testCheckUniqueEditReturnOK(){
        Integer id = 1;
        String name = "Apple";
        String logo = "default.png";
        Brand brand = new Brand(id, name, logo);
        Mockito.when(repo.findByName(name)).thenReturn(brand);

        String result = service.checkUnique(1, "Apple");
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueEditBrandReturnOK(){
        Integer id = 1;
        String name = "Sumsung";
        String logo = "default.png";
        Brand brand = new Brand(id, name, logo);
        Mockito.when(repo.findByName(name)).thenReturn(null);

        String result = service.checkUnique(1, "Sumsung Electronics");
        assertThat(result).isEqualTo("OK");
    }

















}
