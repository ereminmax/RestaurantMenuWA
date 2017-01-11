package com.maxeremin.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

/**
 * Reads the data from XML and puts it to the list
 * Contains different methods which implements CRUD operations with the data
 * @author Max Eremin
 * @since 1.0
 */
public class Model implements ModelInterface{
    private static Model instance = null;
    private Menu menu;
    private Types types;
    private static final Logger logger = LogManager.getLogger();
    private SearchValidator searchValidator = new SearchValidator();
    private Serializer serializer;
    private File file;

    private Model() {
    }

    /**
     * Read type from XML file using DOM parser and puts data to the {@link Menu#menu list}
     * @throws Exception if some problems appeared
     */

    public void readTypes() throws Exception{
        if (!(types == null)) {
            return;
        }

        try {
            types = new Types();
            String path = "src\\main\\resources\\types.xml";
            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);

            NodeList nList = doc.getElementsByTagName("type_item");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nNode;

                    int id = Integer.parseInt(el.getAttribute("id"));
                    String value = el.getTextContent().trim();

                    types.addTypeItem(new TypeItem(id, value));
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred while reading types", e);
            throw new Exception("Exception occurred while reading types");
        }
    }

    /**
     * Reads the menu from another XML file using DOM parser and checks if another file matches or not the existing one
     * Puts data to the {@link Menu#menu list}
     * @throws Exception if some problems appeared
     */

    public void readMenus() throws Exception{
        if (menu == null) {
            readMenu();
        }

        try {
            String path = "src\\main\\resources\\menu2.xml";
            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);

            NodeList nList = doc.getElementsByTagName("menu_item");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nNode;

                    String name = el.getElementsByTagName("name").item(0).getTextContent().trim();
                    int dishType = Integer.parseInt(el.getElementsByTagName("dishType").item(0).getTextContent().trim());
                    double price = Double.parseDouble(el.getElementsByTagName("price").item(0).getTextContent().trim());

                    MenuItem newMI = new MenuItem(name, dishType, price);
                    if (!menu.getMenu().contains(newMI)) {
                        menu.addMenuItem(newMI);
                    }
                    save();
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred while reading another file", e);
            throw new Exception("Exception occurred while reading another file");
        }
    }

    /**
     * Reads the menu from XML using DOM parser and puts it to the {@link Menu#menu list}
     * @throws Exception if some problems appeared
     */

    private void readMenu() throws Exception{
        if (!(menu == null)) {
            return;
        }

        try {
            menu = new Menu();
            String path = "src\\main\\resources\\menu.xml";
            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);

            NodeList nList = doc.getElementsByTagName("menu_item");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nNode;

                    String name = el.getElementsByTagName("name").item(0).getTextContent().trim();
                    int dishType = Integer.parseInt(el.getElementsByTagName("dishType").item(0).getTextContent().trim());
                    double price = Double.parseDouble(el.getElementsByTagName("price").item(0).getTextContent().trim());

                    menu.addMenuItem(new MenuItem(name, dishType, price));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error("Exception occurred while reading menu", e);
            throw new Exception("Exception occurred while reading menu");
        }
    }

    /**
     * Searches the target item in the menu using the name
     * @param name Name of the target item from the menu
     * @return Returns the data of the item or the information if the date does not exist
     * @throws Exception if some problems caused while reading {@link #readTypes() types} or {@link #readMenu() menu}
     */

    public String search(String name) throws Exception{
        readTypes();
        readMenu();

        if (!searchValidator.validate(name)) {
            return "Error: Target sentence must contain cyrillic letters \" - or spaces!";
        }

        // To remove duplicate white spaces
        String cleanName = name.replaceAll("\\s+"," ");

        for(MenuItem el : menu.getMenu()) {
            if (cleanName.equalsIgnoreCase(el.getName())) return el.toString();
        }

        return "Not found! ";
    }

    /**
     * Updates the data about the existing items from the menu
     * @param name name of existing item
     * @param newName new name of the target item
     * @param type new type of the target item
     * @param price new price of the targetn item
     * @throws Exception if the existing or the new names of the item does not match the
     * {@link SearchValidator#SEARCH_PATTERN pattern}
     */

    public void update(String name, String newName, int type, double price) throws Exception {
        readTypes();
        readMenu();

        if (!searchValidator.validate(name) || !searchValidator.validate(newName)) {
            throw new Exception("Error: Target sentences must contain cyrillic letters \" - or spaces!");
        }

        for (int i = 0; i < menu.getMenu().size(); i++) {
            if (name.replaceAll("\\s+", " ").equalsIgnoreCase(menu.getMenu().get(i).getName())) {
                if (newName.equals("")) menu.getMenu().set(i, new MenuItem(name, type, price));
                else menu.getMenu().set(i, new MenuItem(newName, type, price));
                save();
                return;
            }
        }
        throw new Exception("Dish with specified name does not exist");
    }

    /**
     * Adds new item to the {@link Menu#menu list}
     * @param name name of the new item
     * @param type type of the new item
     * @param price price of the new item
     * @see MenuItem
     * @see SearchValidator
     * @throws Exception if the name of the target item does not match the {@link SearchValidator#SEARCH_PATTERN pattern}
     */

    public void add(String name, int type, double price) throws Exception {
        readTypes();
        readMenu();

        if (!searchValidator.validate(name)) {
            throw new Exception("Error: Target sentence must contain cyrillic letters \" - or spaces!");
        }

        String cleanName = name.replaceAll("\\s+"," ");

        menu.addMenuItem(new MenuItem(cleanName, type, price));
        save();
    }

    /**
     * Removes the item from the menu list
     * @param name Name of the item you want to delete from the menu
     * @throws Exception if the target item does not exist in the list
     */

    public void remove(String name) throws Exception{
        readTypes();
        readMenu();

        for (int i = 0; i < menu.getMenu().size(); i++) {
            if (name.replaceAll("\\s+", " ").equalsIgnoreCase(menu.getMenu().get(i).getName())) {
                menu.getMenu().remove(i);
                save();
                return;
            }
        }
        throw new InputMismatchException("Dish with specified name does not exist");
    }

    /**
     * Saves {@link Menu#menu menu} to the XML file using Simple XML Serializer framework
     * @throws Exception
     */

    public void save() throws Exception{
        try {
            readTypes();
            readMenu();

            serializer = new Persister();
            file = new File("src\\main\\resources\\menu.xml");
            serializer.write(menu, file);
        } catch (Exception e) {
            logger.error("Exception occurred while writing the file", e);
            throw new Exception("Exception occurred while writing the file");
        }
    }

    /**
     * Gets the list of the menu
     * @return String array of the menu {@link Menu#menu list}
     */

    public String[] getMenu() {
        final int N = menu.getMenu().size()*3;
        String[] menuArr = new String[N];
        int i = 0;
        
        for (MenuItem mi: menu.getMenu()) {
                menuArr[i] = mi.getName();

                for (TypeItem ti: types.getTypes()) {
                    if (ti.getId() == mi.getDishType()) {
                        menuArr[++i] = ti.getValue();
                    }
                }
                menuArr[++i] = String.valueOf(mi.getPrice());
                i++;
        }
        
        return menuArr;
    }

    public static synchronized Model getInstance() {
        if (instance == null) instance = new Model();
        return instance;
    }
}
