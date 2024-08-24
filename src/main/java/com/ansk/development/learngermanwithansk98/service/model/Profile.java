package com.ansk.development.learngermanwithansk98.service.model;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

/**
 * Temporary implementation for testing purposes.
 *
 * @author Anton Skripin
 */
public class Profile extends AbstractCommandModel<Profile> {
    private String name;
    private String surname;
    private int age;

    private Profile() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private final LinkedHashMap<String, BiConsumer<Profile, String>> mapping = new LinkedHashMap<>();


    @Override
    public LinkedHashMap<String, BiConsumer<Profile, String>> getMapping() {
        return mapping;
    }

    @Override
    public Profile addMapping(String key, BiConsumer<Profile, String> mapping) {
        this.mapping.put(key, mapping);
        return this;
    }

    @Override
    public void append(String awaitingKey, String input) {
        getMapping().get(awaitingKey).accept(this, input);
    }

    public static AbstractCommandModel<Profile> init() {
        return new Profile();
    }


}
