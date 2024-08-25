package com.ansk.development.learngermanwithansk98.service.model;

public class Navigation {
    private boolean next;
    private boolean previous;

    private Navigation() {

    }

    public static Navigation next() {
        Navigation navigation = new Navigation();
        navigation.next = true;
        return navigation;
    }

    public static Navigation previous() {
        Navigation navigation = new Navigation();
        navigation.previous = true;
        return navigation;
    }

    public boolean isNext() {
        return next;
    }

    public boolean isPrevious() {
        return previous;
    }

}
