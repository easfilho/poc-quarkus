package org.acme.api.dto;

import org.acme.enums.SizeEnum;

public class DogInputDto {

    private String name;
    private Integer age;
    private String race;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DogInputDto{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", race='").append(race).append('\'');
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}
