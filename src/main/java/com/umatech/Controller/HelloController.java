package com.umatech.Controller;

import com.umatech.DB.DatabaseManager;
import com.umatech.TomcatApplication.TomcatApplicationRunner;
import com.umatech.TomcatApplication.annotation.RequestMapping;
import com.umatech.TomcatApplication.annotation.Resource;
import com.umatech.TomcatApplication.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RestController("/controller")
public class HelloController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    public DatabaseManager databaseManager;

    @RequestMapping(value = "/method", method = "POST")
    public String testMethod() {
        List<String> res = new ArrayList<>();
        try{
            logger.info(databaseManager);
            Connection connection = databaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT now() as time"; // 替换为你的表名
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("Column Value: " + resultSet.getString("time"));
                res.add(resultSet.getString("time"));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return res.get(0);
    }
}
