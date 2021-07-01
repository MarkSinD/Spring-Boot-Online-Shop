package com.shopme.shopmebackend.setting.state;

import com.shopme.common.entity.Country;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class StateDTO {
    Integer id;
    String name;
    Country country;

    public StateDTO() {

    }

    public StateDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public StateDTO(Integer id, String name, Country country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
