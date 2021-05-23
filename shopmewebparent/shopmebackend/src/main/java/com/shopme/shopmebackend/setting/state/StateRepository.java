package com.shopme.shopmebackend.setting.state;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public interface StateRepository extends CrudRepository<State, Integer> {
    List<State> findByCountryOrderByNameAsc(Country country);
}
