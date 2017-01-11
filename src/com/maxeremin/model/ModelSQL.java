package com.maxeremin.model;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Max Eremin
 * @since 2.0
 */
public class ModelSQL implements ModelInterface{

    private DataSource dataSource;
    private SearchValidator searchValidator = new SearchValidator();
    private static final Logger logger = LogManager.getLogger();

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void readTypes() throws Exception {
    }

    @Override
    public void readMenus() throws Exception {
    }

    @Override
    public String search(String name) throws Exception {
        MenuItem mi = null;

        if (!searchValidator.validate(name)) {
            return "Error: Target sentence must contain cyrillic letters \" - or spaces!";
        }

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = searchStatement(con, name);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                mi = new MenuItem(
                        rs.getString("name"),
                        rs.getInt("dishtype"),
                        rs.getDouble("price")
                );
            }

            if (mi == null) {
                return "Not found!";
            }

            return mi.toString();
        }
    }

    private PreparedStatement searchStatement(Connection con, String name) throws SQLException{
        String sql = "SELECT * FROM MENU WHERE NAME = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        return ps;
    }

    @Override
    public void add(String name, int type, double price) throws Exception {
        if (!searchValidator.validate(name)) {
            logger.error("Error: Target sentence must contain cyrillic letters \" - or spaces!");
            throw new Exception("Error: Target sentence must contain cyrillic letters \" - or spaces!");
        }

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = addStatement(con, name, type, price)) {
            ps.executeUpdate();
        }
    }

    private PreparedStatement addStatement(Connection con, String name, int type, double price) throws SQLException {
        String sql = "insert into menu (name, dishtype, price) values (?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, type);
        ps.setDouble(3, price);

        return ps;
    }

    @Override
    public void remove(String name) throws Exception {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = deleteStatement(con, name)) {

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                logger.error("Dish with specified name does not exist");
                throw new Exception("Dish with specified name does not exist");
            }
        }
    }

    private PreparedStatement deleteStatement(Connection con, String name) throws SQLException {
        String sql = "DELETE FROM MENU WHERE NAME = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        return ps;
    }

    @Override
    public void update(String name, String newName, int type, double price) throws Exception {
        if (!searchValidator.validate(name) || !searchValidator.validate(newName)) {
            logger.error("Error: Target sentences must contain cyrillic letters \" - or spaces!");
            throw new Exception("Error: Target sentences must contain cyrillic letters \" - or spaces!");
        }

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = updateStatement(con, name, newName, type, price)) {

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                logger.error("Dish with specified name does not exist");
                throw new Exception("Dish with specified name does not exist");
            }
        }
    }

    private PreparedStatement updateStatement(Connection con, String name, String newName, int type, double price) throws SQLException {
        String sql = "UPDATE MENU SET NAME = ?, DISHTYPE = ?, PRICE = ? WHERE NAME = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        if (newName.equals("")) {
            ps.setString(1, name);
            ps.setInt(2, type);
            ps.setDouble(3, price);
            ps.setString(4, name);
        } else {
            ps.setString(1, newName);
            ps.setInt(2, type);
            ps.setDouble(3, price);
            ps.setString(4, name);
        }

        return ps;
    }

    @Override
    public void save() throws Exception {
    }

    @Override
    public String[] getMenu() {
        LinkedList<String> ls = new LinkedList<String>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = printStatement(con);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ls.addLast(rs.getString(2));
                ls.addLast(rs.getString(3));
                ls.addLast(rs.getString(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls.toArray(new String[ls.size()]);
    }

    private PreparedStatement printStatement(Connection con) throws SQLException{
        String sql = "SELECT * FROM MENU";
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }
}
