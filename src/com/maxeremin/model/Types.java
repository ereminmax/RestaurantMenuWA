package com.maxeremin.model;

import java.util.LinkedList;

/**
 * List of {@link #types types} contains different items
 * @see TypeItem
 * @author Max Eremin
 * @since 1.0
 */
public class Types {
    private LinkedList<TypeItem> types = new LinkedList<>();

    public Types() {
    }

    /**
     * Adds new item
     * @param NewTypeItem Item you want to add to the {@link #types list}
     */

    public void addTypeItem(TypeItem NewTypeItem) {
        types.addLast(NewTypeItem);
    }

    public LinkedList<TypeItem> getTypes() {
        return types;
    }
}
