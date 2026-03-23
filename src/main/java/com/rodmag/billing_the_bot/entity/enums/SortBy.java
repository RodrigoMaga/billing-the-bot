package com.rodmag.billing_the_bot.entity.enums;

public enum SortBy {

    YEAR("year"),
    MONTH("month"),
    PARTICIPANT_NAME("participant.name");

    private final String property;

    SortBy(String property) {
        this.property = property;
    }

    public String property() {
        return property;
    }
}