package com.shopme.shopmebackend.setting.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;
import com.shopme.shopmebackend.setting.country.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class StateRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    CountryRepository countryRepository;

    @Test
    @WithMockUser(username = "high-school@bk.ru", password = "11111111", roles = "ADMIN")
    public void testListStates() throws Exception{
        String url = "/states/list/1";

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        StateDTO[] states = objectMapper.readValue(jsonResponse, StateDTO[].class);
        assertThat(states).hasSizeGreaterThan(0);
    }

    @Test
    public void testCreateStates() throws Exception {
        String url = "/states/save";
        Integer countryId = 1;
        Country country = countryRepository.findById(countryId).get();

        State state = new State("California", country);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(state))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(response);
        Optional<State> findById = stateRepository.findById(stateId);

        assertThat(findById.isPresent());
    }

    @Test
    public void testUpdateStates() throws Exception {

        String url = "/states/save";
        Integer countryId = 1;
        Country country = countryRepository.findById(countryId).get();

        State state = new State("Aowa", country);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(state))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(response);
        Optional<State> findById = stateRepository.findById(stateId);

        assertThat(findById.get().getName()).isEqualTo(state.getName());
    }

    @Test
    public void testDeleteState() throws Exception {
        Integer stateId = 8;
        String url = "/states/delete/" + stateId;
        mockMvc.perform(get(url)).andExpect(status().isOk());
        Optional<State> findById = stateRepository.findById(stateId);
        assertThat(findById).isNotPresent();
    }
}
