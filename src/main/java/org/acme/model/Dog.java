package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.acme.enums.SizeEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Dog extends PanacheEntity {

    private String name;
    private Integer age;
    private String race;
    @Enumerated(EnumType.STRING)
    private SizeEnum size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public SizeEnum getSize() {
        return size;
    }

    public void setSize(SizeEnum size) {
        this.size = size;
    }
}
