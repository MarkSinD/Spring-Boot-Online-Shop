package com.shopme.shopmebackend.setting.state;

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
@RequestMapping("states")
public class StateRestController {

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/list/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
        List<State> stateList = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> dtoList = new ArrayList<>();

        for(State state : stateList){
            dtoList.add(new StateDTO(state.getId(), state.getName()));
        }
        return dtoList;
    }

    @PostMapping("/save")
    public String save(@RequestBody State state){
        State savedState = stateRepository.save(state);
        return String.valueOf(savedState.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer stateId){
        stateRepository.deleteById(stateId);
    }
}
