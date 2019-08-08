package com.eks.utils;

import com.eks.enumeration.DatabaseDriverEnum;
import com.google.gson.JsonArray;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JdbcUtils {
    public static void loadDatabaseDriver(DatabaseDriverEnum databaseDriverEnum) throws ClassNotFoundException {
        String databaseDriverNameString = null;
        if (databaseDriverEnum == DatabaseDriverEnum.MYSQL){
            databaseDriverNameString = "com.mysql.jdbc.Driver";
        }else if (databaseDriverEnum == DatabaseDriverEnum.ORACLE){
            databaseDriverNameString = "oracle.jdbc.driver.OracleDriver";
        }else if (databaseDriverEnum == DatabaseDriverEnum.SQLITE){
            databaseDriverNameString = "org.sqlite.JDBC";
        }
        Class.forName(databaseDriverNameString);
    }
    public static JsonArray queryJsonArrayByJdbc(DatabaseDriverEnum databaseDriverEnum, String urlString, String usernameString, String passwordString, String sqlString) throws Exception{
        List<Map<String, Object>> mapList = queryByJdbc(databaseDriverEnum,urlString,usernameString,passwordString,sqlString);
        String mapListJsonString = GsonUtils.convertObjectToJsonString(mapList);
        return GsonUtils.convertJsonStringToJsonElement(mapListJsonString).getAsJsonArray();
    }
    public static List<Map<String,Object>> queryByJdbc(String urlString,String usernameString,String passwordString,String sqlString) throws Exception{
        return queryByJdbc(DatabaseDriverEnum.MYSQL,urlString,usernameString,passwordString,sqlString);
    }
    public static List<Map<String,Object>> queryByJdbc(DatabaseDriverEnum databaseDriverEnum,String urlString,String usernameString,String passwordString,String sqlString) throws Exception{
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            loadDatabaseDriver(databaseDriverEnum);
            connection = DriverManager.getConnection(urlString, usernameString, passwordString);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlString);
            //获取键名
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            //获取行的数量
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()){
                //使用TreeMap保证顺序,一个Map相当于数据库中一行数据(相当于该表的enity)
                Map<String,Object> rowData = new TreeMap<String,Object>();
                //请注意，列序号从 1 开始，而不是从 0 开始
                for (int i = 1; i <= columnCount; i++) {
                    //获取键名及值
                    rowData.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }
                list.add(rowData);
            }
        }finally {
            AutoCloseableUtils.close(resultSet);
            AutoCloseableUtils.close(statement);
            AutoCloseableUtils.close(connection);
        }
        return list;
    }
    public static int[] updateByJdbc(String urlString,String usernameString,String passwordString,List<String> sqlStringList) throws Exception {
        return updateByJdbc(DatabaseDriverEnum.MYSQL,urlString,usernameString,passwordString,sqlStringList);
    }
    public static int[] updateByJdbc(DatabaseDriverEnum databaseDriverEnum,String urlString,String usernameString,String passwordString,List<String> sqlStringList) throws Exception {
        int[] updateCountIntArray = null;
        Connection connection = null;
        Statement statement = null;
        Exception exception = null;
        try {
            loadDatabaseDriver(databaseDriverEnum);
            connection = DriverManager.getConnection(urlString, usernameString, passwordString);
            statement = connection.createStatement();
            if(connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            for (String sqlString : sqlStringList) {
                statement.addBatch(sqlString);
            }
            updateCountIntArray = statement.executeBatch();
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        }catch (Exception e){
            exception = e;
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
            }
        }finally {
            AutoCloseableUtils.close(statement);
            if (connection != null && !connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
            AutoCloseableUtils.close(connection);
        }
        if(exception != null){
            throw exception;
        }
        return updateCountIntArray;
    }
}