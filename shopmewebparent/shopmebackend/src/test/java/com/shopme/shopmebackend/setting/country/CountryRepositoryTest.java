package com.shopme.shopmebackend.setting.country;

import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CountryRepositoryTest {
    @Autowired
    CountryRepository countryRepository;

    @Test
    public void testCreateCountry(){
        Country country = new Country("USA", "350089",  null);
        Country savedCountry = countryRepository.save(country);
        assertThat(savedCountry.getId()).isEqualTo(1);
    }

    @Test
    public void testUpdateCountries(){
        Optional<Country> optionalCountry = countryRepository.findById(1);
        if(optionalCountry.isPresent()){
            Country country = optionalCountry.get();
            country.setName("America");
            Country savedCountry = countryRepository.save(country);
            assertThat(savedCountry.getName()).isEqualTo("America");
        }
        else{
            assertThat(false);
        }
    }

    @Test
    public void testFindAllByOrderByNameAsc(){
        List<Country> countryList = countryRepository.findAllByOrderByNameAsc();
        System.out.println(countryList);
    }
}
