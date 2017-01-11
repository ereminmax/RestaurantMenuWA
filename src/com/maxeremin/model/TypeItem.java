package com.maxeremin.model;

/**
 * Each instance of this class contains {@link #id id} and {@link #value value} of one dish type
 * @author Max Eremin
 * @since 1.0
 */
class TypeItem {
    private int id;
    private String value;

    TypeItem(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
