package com.maxeremin.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.LinkedList;

/**
 * This class is serialized using XML Simple Serializer framework
 * It contains the {@link #menu list} of the menu
 * @author Max Eremin
 * @since 1.0
 */
@Root(name = "menu_list")
public class Menu {
    @ElementList(inline = true)
    private LinkedList<MenuItem> menu = new LinkedList<>();

    public Menu() {
    }

    /**
     * Adds new item to the menu
     * @param NewMenuItem new item of the menu you want to add to the {@link #menu list}
     */

    public void addMenuItem(MenuItem NewMenuItem) {
        menu.addLast(NewMenuItem);
    }

    public LinkedList<MenuItem> getMenu() {
        return menu;
    }

}
