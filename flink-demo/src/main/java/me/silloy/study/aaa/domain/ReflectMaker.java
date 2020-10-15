package me.silloy.study.aaa.domain;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: lizelong
 * @date: 2019/12/2
 * @description:
 **/
public class ReflectMaker implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectMaker.class);
    private static HikariDataSource ds;

    static {
        try {

            ParameterTool parameterTool = ParameterTool.fromPropertiesFile("/Users/lizelong/flink-1.9.1/conf/tracker.properties");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl ("jdbc:mysql://"+parameterTool.getRequired("db_ip_host")+"/"+parameterTool.getRequired("db_database")+"?serverTimezone=GMT%2b8:00" +
                    "&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&allowMultiQueries=true&useSSL=false");
            config.setUsername (parameterTool.getRequired("db_user"));
            config.setPassword (parameterTool.getRequired("db_password").equals ("__NO_VALUE_KEY")?"":parameterTool.getRequired("db_password"));
            config.setMaxLifetime (604800);
            config.setMaximumPoolSize (18);

            ds = new HikariDataSource (config);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public static boolean query(String sql) throws Exception{

        LOG.info ("node excute sql is: {}",sql);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next ();
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public List<?> query(String sql, Class<?> c) throws Exception{

        LOG.info ("node excute sql is: {}",sql);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            return getDatas(resultSet,c);
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private List<?> getDatas(ResultSet resultSet, Class<?> c) throws Exception {

        List<Object> list = new ArrayList<> ();
        if (resultSet == null) {
            return null;
        }
        ResultSetMetaData rsmd = resultSet.getMetaData();
        Field[] fields = c.getDeclaredFields ();
        ResultSetMetaData metaData = resultSet.getMetaData();

        while (resultSet.next()) {
            Object bean = c.newInstance ();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                List<Field> fieldList = Arrays.asList (fields);
                Field field = fieldList.stream ().filter (p -> equalsIgnoreDB (p.getName (), columnName)).findAny ().orElse (null);
                if (field != null) {
                    BeanUtils.setProperty(bean, field.getName (), columnValue);
                }
            }
            list.add(bean);
        }
        return list;
    }

    private boolean equalsIgnoreDB(String fieldName, String columnName) {

        String s1 = columnName.replaceAll ("_","");
        if (s1.equalsIgnoreCase (fieldName)) {
            return true;
        }
        return false;
    }


}
