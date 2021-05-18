package com.shopme.shopmebackend.country;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
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
public class StateRepositoryTest {
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateState(){
        Country country = entityManager.find(Country.class, 1);
        State state = new State("Alabama", country);
        State savedState = stateRepository.save(state);
        assertThat(savedState.getId()).isEqualTo(1);
    }

    @Test
    public void testListStatesByCountry(){
        List<State> list = new ArrayList<>();
        Country country = entityManager.find(Country.class, 1);
        State alabama = new State("Alabama", country);
        State arkansas = new State("Arkansas", country);
        State arizona = new State("Arizona", country);
        State california = new State("California", country);
        State colorado = new State("Colorado", country);
        list.add(alabama);
        list.add(arkansas);
        list.add(arizona);
        list.add(california);
        list.add(colorado);
        Iterable<State> states = stateRepository.saveAll(list);
        assertThat(states).isNotEmpty();
    }

    @Test
    public void testUpdateState(){
        Optional<State> stateOptional = stateRepository.findById(1);
        if(stateOptional.isPresent()){
            State state = stateOptional.get();
            state.setName("Montana");
            State savedState = stateRepository.save(state);
            assertThat(savedState.getName()).isEqualTo("Montana");
        }
    }

    @Test
    public void testGetState(){
        Iterable<State> states = stateRepository.findAll();
        states.forEach(System.out::println);
    }

    @Test
    public void testDeleteState(){
        Optional<State> stateOptional = stateRepository.findById(6);
        if(stateOptional.isPresent()){
            State state = stateOptional.get();
            stateRepository.delete(state);
        }

        Optional<State> deletedStateOptional = stateRepository.findById(6);
        if(deletedStateOptional.isPresent()){
            assertThat(false);
        }
        else{
            assertThat(true);
        }
    }



}
