package com.shopme.common.shopmefrontend.settings;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@RestController
public class StateRestController {

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
        List<State> stateList = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> dtoList = new ArrayList<>();

        for(State state : stateList){
            dtoList.add(new StateDTO(state.getId(), state.getName()));
        }
        System.out.println(dtoList);
        return dtoList;
    }

}
