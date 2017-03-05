/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bari.jersey.service;

import com.bari.jersey.bean.Country;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public class CountryService {

    static HashMap<Integer, Country> countryIdMap = getCountryIdMap();

    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/restful_db", "root", "123");
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

    public List<Map<String, String>> getAllCountries() {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("select * from country");
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Map map = new HashMap();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String key = meta.getColumnName(i);
                    String value = rs.getString(key);
                    map.put(key, value);
                }
                list.add(map);

            }
            System.out.println(list);
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public Country getCountry(int id) {
        Country country = countryIdMap.get(id);

        if (country == null) {
            throw new CountryNotFoundException("Country with id " + id + " not found");
        }
        return country;
    }

    public Country addCountry(Country country) {
//        country.setId(getMaxId() + 1);
//        countryIdMap.put(country.getId(), country);
//        return country;
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("insert into country (countryName, population)values(?,?)");
            ps.setString(1, country.getCountryName());
            ps.setString(2, country.getPopulation());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
        return country;
    }

    public Country updateCountry(Country country) {
//        if (country.getId() <= 0) {
//            return null;
//        }
//        countryIdMap.put(country.getId(), country);
//        return country;
        if (country.getId() <= 0) {
            return null;
        }

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("update country set countryName=?, population=? where id=?");
            ps.setString(1, country.getCountryName());
            ps.setString(2, country.getPopulation());
            ps.setInt(3, country.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
        return country;
    }

    public void deleteCountry(int id) {
//        countryIdMap.remove(id);
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("delete from country where id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static HashMap<Integer, Country> getCountryIdMap() {
        return countryIdMap;
    }

    // Utility method to get max id
    public static int getMaxId() {
        int max = 0;
        for (int id : countryIdMap.keySet()) {
            if (max <= id) {
                max = id;
            }

        }
        return max;
    }

}
